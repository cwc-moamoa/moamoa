package com.teamsparta.moamoa.domain.order.service

import com.teamsparta.moamoa.domain.groupPurchase.model.GroupPurchaseEntity
import com.teamsparta.moamoa.domain.groupPurchase.model.GroupPurchaseJoinUserEntity
import com.teamsparta.moamoa.domain.groupPurchase.repository.GroupPurchaseJoinUserRepository
import com.teamsparta.moamoa.domain.groupPurchase.repository.GroupPurchaseRepository
import com.teamsparta.moamoa.domain.order.dto.CancelResponseDto
import com.teamsparta.moamoa.domain.order.dto.ResponseOrderDto
import com.teamsparta.moamoa.domain.order.dto.UpdateOrderDto
import com.teamsparta.moamoa.domain.order.model.OrdersEntity
import com.teamsparta.moamoa.domain.order.model.OrdersStatus
import com.teamsparta.moamoa.domain.order.model.toResponse
import com.teamsparta.moamoa.domain.order.repository.OrderRepository
import com.teamsparta.moamoa.domain.payment.model.PaymentEntity
import com.teamsparta.moamoa.domain.payment.model.PaymentStatus
import com.teamsparta.moamoa.domain.payment.repository.PaymentRepository
import com.teamsparta.moamoa.domain.product.model.Product
import com.teamsparta.moamoa.domain.product.model.ProductStock
import com.teamsparta.moamoa.domain.product.repository.ProductRepository
import com.teamsparta.moamoa.domain.product.repository.ProductStockRepository
import com.teamsparta.moamoa.domain.seller.repository.SellerRepository
import com.teamsparta.moamoa.domain.socialUser.model.SocialUser
import com.teamsparta.moamoa.domain.socialUser.repository.SocialUserRepository
import com.teamsparta.moamoa.exception.ModelNotFoundException
import com.teamsparta.moamoa.infra.redis.RedissonLockManager
import org.springframework.data.domain.Page
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Service
class OrderServiceImpl(
    private val orderRepository: OrderRepository,
    private val productRepository: ProductRepository,
    private val productStockRepository: ProductStockRepository,
    private val sellerRepository: SellerRepository,
    private val paymentRepository: PaymentRepository,
    private val groupPurchaseRepository: GroupPurchaseRepository,
    private val socialUserRepository: SocialUserRepository,
    private val groupPurchaseJoinUserRepository: GroupPurchaseJoinUserRepository,
    private val redissonLockManager: RedissonLockManager
) : OrderService {

    @Transactional
    override fun createOrder(
        userId: Long,
        productId: Long,
        quantity: Int,
        address: String,
        phoneNumber: String,
    ): ResponseOrderDto {
        val lockKey = "createOrderWithLock_$productId"
        val lockAcquired = redissonLockManager.acquireLock(lockKey, 15000, 60000)
        if (!lockAcquired) {
            throw Exception("락을 획득할 수 없습니다. 잠시 후 다시 시도해주세요.")
        }

        try {
            val (findUser, findProduct, stockCheck) = orderCommon(userId, productId, quantity)

            val totalPrice = findProduct.price * quantity
            val finalDiscount = 0.0

            return orderSave(
                findUser,
                findProduct,
                stockCheck,
                totalPrice,
                finalDiscount,
                address,
                quantity,
                phoneNumber
            ).toResponse()
        } finally {
            redissonLockManager.releaseLock(lockKey)
        }
    }

    @Transactional // 테스트용 락 없는 코드
    override fun createOrderNoLock(
        userId: Long,
        productId: Long,
        quantity: Int,
        address: String,
        phoneNumber: String,
    ): ResponseOrderDto {
            val (findUser, findProduct, stockCheck) = orderCommon(userId, productId, quantity)

            val totalPrice = findProduct.price * quantity
            val finalDiscount = 0.0

            return orderSave(
                findUser,
                findProduct,
                stockCheck,
                totalPrice,
                finalDiscount,
                address,
                quantity,
                phoneNumber
            ).toResponse()
        }

    @Transactional
    override fun createGroupOrder(
        userId: Long,
        productId: Long,
        quantity: Int,
        address: String,
        phoneNumber: String,
    ): ResponseOrderDto {
        val (findUser, findProduct, stockCheck) = orderCommon(userId, productId, quantity)
        val finalDiscount = findProduct.discount

        val groupPurchaseCheck = groupPurchaseRepository.findByProductIdAndDeletedAtIsNull(productId) // 없으면 유저도 없는거니께
        val groupPurchaseUserCheck = groupPurchaseCheck?.groupPurchaseUsers?.find { it.userId == findUser.id }

        if (groupPurchaseUserCheck == null) {
            val totalPrice = findProduct.price * quantity * (1 - findProduct.discount / 100.0)

            return orderSave(findUser, findProduct, stockCheck, totalPrice, finalDiscount, address, quantity, phoneNumber).toResponse()
        } else {
            throw Exception("이미 공동 구매 신청중인 유저는 주문을 신청 할 수 없습니다.")
        }
    }

    private fun orderSave(
        findUser: SocialUser,
        findProduct: Product,
        stockCheck: ProductStock,
        totalPrice: Double,
        finalDiscount: Double,
        address: String,
        quantity: Int,
        phoneNumber: String,
    ): OrdersEntity {
        val payment = PaymentEntity(price = totalPrice, status = PaymentStatus.READY)
        paymentRepository.save(payment)
        val order =
            OrdersEntity(
                productName = findProduct.title,
                totalPrice = totalPrice,
                address = address,
                discount = finalDiscount,
                product = findProduct,
                quantity = quantity,
                socialUser = findUser,
                orderUid = UUID.randomUUID().toString(),
                payment = payment,
                phoneNumber = phoneNumber,
                sellerId = findProduct.seller.id,
            )
        productStockRepository.save(stockCheck.discountForTest(quantity))
//        stockCheck.discount(quantity)
        return orderRepository.save(order)
    }

    private fun orderCommon(
        userId: Long,
        productId: Long,
        quantity: Int,
    ): Triple<SocialUser, Product, ProductStock>  {
        val findUser = socialUserRepository.findByIdOrNull(userId) ?: throw Exception("존재하지 않는 유저입니다")
        val findProduct =
            productRepository.findByIdAndDeletedAtIsNull(productId).orElseThrow { Exception("존재하지 않는 상품입니다") }
        val stockCheck = productStockRepository.findByProduct(findProduct)

        if (stockCheck!!.stock <= quantity) throw Exception("재고가 모자랍니다. 판매자에게 문의해주세요")
        return Triple(findUser, findProduct, stockCheck)
    }

    @Transactional
    override fun updateOrder(
        userId: Long,
        orderId: Long,
        updateOrderDto: UpdateOrderDto,
    ): ResponseOrderDto {
        val findUser = socialUserRepository.findByIdOrNull(userId) ?: throw ModelNotFoundException("user", userId)
        val findOrders = orderRepository.findByIdAndDeletedAtIsNull(orderId).orElseThrow { Exception("존재하지 않는 주문입니다") }
        // 논리삭제가 된 주문은 업데이트 할 필요가 없기 때문에 찾지 않도록 함
        if (findOrders.socialUser == findUser) {
            findOrders.address = updateOrderDto.address
            return orderRepository.save(findOrders).toResponse()
        } else {
            throw Exception("도둑 검거 완료")
        }
    }

    @Transactional
    override fun cancelOrder(
        userId: Long,
        orderId: Long,
    ): CancelResponseDto {
        val findUser = socialUserRepository.findByIdOrNull(userId) ?: throw ModelNotFoundException("user", userId)
        val findOrder = orderRepository.findByIdAndDeletedAtIsNull(orderId).orElseThrow { Exception("존재하지 않는 주문입니다") }
        val stock = productStockRepository.findByProduct(findOrder.product)

        // 요서 부터는 공구친구들을 위한
        val findGroupJoinUser =
            groupPurchaseJoinUserRepository.findByOrderId(orderId) ?: throw ModelNotFoundException(
                "groupJoinUser", orderId,
            )
        val group = findGroupJoinUser.groupPurchase // 방
        val groupLimit = group.userLimit // 그룹방 유저 리밋
        val groupUserCount = group.userCount // 그룹방 유저카운트
        val payInfo = findOrder.payment // 결제정보

        // 이미 취소된 주문을 또 찾으면 안되기 때문에 논리삭제가 된 것은 찾지 않도록 함
        if (findUser.id != findOrder.socialUser.id) {
            throw Exception("주문정보가 일치하지 않습니다")
        }

        if (findOrder.status == OrdersStatus.CANCELLED) {
            throw Exception("이미 취소된 주문입니다.")
        }
        // 공구 인지 아닌지
        if (findOrder.discount > 0.0) {
            if (groupLimit == groupUserCount) {
                throw Exception("매칭이 완료되었기 때문에 취소가 불가합니다.")
                // 더 좋은 문장이 안떠오름
            } else if (groupUserCount == 1 // 그룹에 한명만 있을때
            ) {
                cancelEtc(findOrder, stock!!, findGroupJoinUser, payInfo, group)
                group.deletedAt = LocalDateTime.now()
                group.groupPurchaseUsers.remove(findGroupJoinUser)
            } else if (groupLimit > groupUserCount // 그룹이 완성되지 않았지만 여러명일때
            ) {
                cancelEtc(findOrder, stock!!, findGroupJoinUser, payInfo, group)
                group.groupPurchaseUsers.remove(findGroupJoinUser)
            }
        } else {
            findOrder.deletedAt = LocalDateTime.now()
            findOrder.status = OrdersStatus.CANCELLED
            stock!!.stock += findOrder.quantity // ?.을 써서 어떤식으로 넘길지 모르겠음 세이프콜을 쓰면 오히려 재고가 안맞을수도있을거같음
            // 일반 주문일때 재고원래대로 돌려놓고 주문 논리삭제
        }
        return CancelResponseDto(
            message = "주문이 취소 되었습니다",
        )
    }

    private fun cancelEtc(
        findOrder: OrdersEntity,
        stock: ProductStock,
        findGroupJoinUser: GroupPurchaseJoinUserEntity,
        payInfo: PaymentEntity,
        group: GroupPurchaseEntity,
    )  {
        findOrder.deletedAt = LocalDateTime.now()
        findOrder.status = OrdersStatus.CANCELLED
        stock.stock += findOrder.quantity
        findGroupJoinUser.deletedAt = LocalDateTime.now()
        payInfo.deletedAt = LocalDateTime.now()
        group.userCount -= 1
    } // 중복이 넘 많아서 함수로 묶고 다른부분만 따로 정리해줌

    override fun getOrder(
        userId: Long,
        orderId: Long,
    ): ResponseOrderDto {
        val findUser = socialUserRepository.findByIdOrNull(userId) ?: throw ModelNotFoundException("user", userId)
        val findOrder = orderRepository.findByIdOrNull(orderId) ?: throw Exception("존재하지 않는 주문 입니다")
        // 취소된 주문도 조회는 가능해야 한다 생각해서, 논리삭제된것도 찾을수 있게 함
        return if (findOrder.socialUser.id == findUser.id) {
            findOrder.toResponse()
        } else {
            throw Exception("유저와 주문정보가 일치하지 않습니다")
        }
    }


    override fun getOrderPage(
        userId: Long,
        page: Int,
        size: Int,
    ): Page<ResponseOrderDto> {
        return orderRepository.getOrderPage(userId, page, size).map { it.toResponse() }
    }

    @Transactional
    override fun orderStatusChange(
        orderId: Long,
        sellerId: Long,
        status: OrdersStatus,
    ): ResponseOrderDto {
        val findSeller =
            sellerRepository.findByIdAndDeletedAtIsNull(sellerId).orElseThrow { Exception("존재하지 않는 판매자 입니다") }
        val findProductList = productRepository.findBySellerId(findSeller.id!!) // 이거 잘 몰겠음

        if (findProductList.isEmpty()) {
            throw ModelNotFoundException("product", sellerId)
        }
        val findOrder = orderRepository.findByIdAndDeletedAtIsNull(orderId).orElseThrow { Exception("존재하지 않는 주문입니다") }
        // 이건 상태를 변경하는거고, 취소된 주문은 이미 상태가 cancelled 이기 때문에, 상태변경을 지원하지 않음.
        val findResult =
            findProductList.find { it.id == findOrder.product.id } ?: throw Exception("판매자가 파는 상품의 주문이 아닙니다")
        val stock = productStockRepository.findByProduct(findResult)
        if (status == OrdersStatus.CANCELLED) {
            findOrder.status = status
            stock!!.stock += findOrder.quantity
            productStockRepository.save(stock!!)
        } else {
            findOrder.status = status
        }
        return orderRepository.save(findOrder).toResponse()
    }

    override fun getOrderBySellerId(
        sellerId: Long,
        orderId: Long,
    ): ResponseOrderDto {
        val findSeller =
            sellerRepository.findByIdAndDeletedAtIsNull(sellerId).orElseThrow { Exception("존재하지 않는 판매자 입니다") }
        val findOrder = orderRepository.findByIdOrNull(orderId) ?: throw Exception("존재하지 않는 주문 입니다")
        // 셀러는 논리삭제된걸 찾지 않으나, 주문은 논리삭제(취소)되었어도 찾아야 한다 생각하여 찾음

        return if (findOrder.product.seller.id == findSeller.id) {
            findOrder.toResponse()
        } else {
            throw Exception("판매자 불일치")
        }
    }

    override fun getOrderPageBySellerId(
        sellerId: Long,
        page: Int,
        size: Int,
    ): Page<ResponseOrderDto> {
        return orderRepository.getOrderPageBySellerId(sellerId, page, size).map { it.toResponse() }
    } // 이 로직은 취소된것도 알수있어야 한다 생각하여 논리삭제된것도 예외없이 다 검색함
}
