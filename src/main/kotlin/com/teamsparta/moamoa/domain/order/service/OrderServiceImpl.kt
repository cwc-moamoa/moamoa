package com.teamsparta.moamoa.domain.order.service

import com.teamsparta.moamoa.domain.groupPurchase.repository.GroupPurchaseRepository
import com.teamsparta.moamoa.domain.order.dto.*
import com.teamsparta.moamoa.domain.order.model.OrdersEntity
import com.teamsparta.moamoa.domain.order.model.OrdersStatus
import com.teamsparta.moamoa.domain.order.model.toResponse
import com.teamsparta.moamoa.domain.order.repository.OrderRepository
import com.teamsparta.moamoa.domain.payment.model.PaymentEntity
import com.teamsparta.moamoa.domain.payment.model.PaymentStatus
import com.teamsparta.moamoa.domain.payment.repository.PaymentRepository
import com.teamsparta.moamoa.domain.product.model.ProductStock.Companion.discount
import com.teamsparta.moamoa.domain.product.repository.ProductRepository
import com.teamsparta.moamoa.domain.product.repository.ProductStockRepository
import com.teamsparta.moamoa.domain.seller.repository.SellerRepository
import com.teamsparta.moamoa.domain.socialUser.repository.SocialUserRepository
import com.teamsparta.moamoa.exception.ModelNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.UUID

@Service
class OrderServiceImpl(
    private val orderRepository: OrderRepository,
    private val productRepository: ProductRepository,
    private val productStockRepository: ProductStockRepository,
    private val sellerRepository: SellerRepository,
    private val redisTemplate: RedisTemplate<String, Any>,
    private val paymentRepository: PaymentRepository,
    private val groupPurchaseRepository: GroupPurchaseRepository,
    private val socialUserRepository: SocialUserRepository,
) : OrderService {
    // 유저는 추후 providerId 가져오는 문제 및 논리삭제 적용되면 논리삭제된것 안찾기 적용예정
    @Transactional
    override fun createOrder(
        userId: Long,
        productId: Long,
        quantity: Int,
        address: String,
        phoneNumber: String,
    ): ResponseOrderDto {
        val findUser = socialUserRepository.findByIdOrNull(userId) ?: throw Exception("존재하지 않는 유저입니다")
        val findProduct =
            productRepository.findByIdAndDeletedAtIsNull(productId).orElseThrow { Exception("존재하지 않는 상품입니다") }
        val stockCheck = productStockRepository.findByProduct(findProduct)

        if (stockCheck!!.stock < 0 && stockCheck.stock <= quantity) throw Exception("재고가 모자랍니다. 판매자에게 문의해주세요")

        // 어떻게 막을지 생각하기....
        if (findProduct.discount > 0) {
            val groupPurchaseCheck = groupPurchaseRepository.findByProductIdAndDeletedAtIsNull(productId)
            val groupPurchaseUserCheck = groupPurchaseCheck?.groupPurchaseUsers?.find { it.userId == findUser.id }
            if (groupPurchaseUserCheck == null) {
                val discountedPrice = findProduct.price * quantity * (1 - findProduct.discount / 100.0)
                val discountedPayment = PaymentEntity(price = discountedPrice, status = PaymentStatus.READY)
                val discountedOrder =
                    orderRepository.save(
                        OrdersEntity(
                            productName = findProduct.title,
                            totalPrice = discountedPrice,
                            address = address,
                            discount = findProduct.discount,
                            product = findProduct,
                            quantity = quantity,
                            socialUser = findUser,
                            orderUid = UUID.randomUUID().toString(),
                            payment = discountedPayment,
                            phoneNumber = phoneNumber,
                        ),
                    )
                paymentRepository.save(discountedPayment)
                productStockRepository.save(stockCheck.discount(quantity))
                return discountedOrder.toResponse()
            } else {
                throw Exception("이미 공동 구매 신청중인 유저는 주문을 신청 할 수 없습니다.")
            }
        } else {
            val payment =
                PaymentEntity(
                    price = findProduct.price * quantity,
                    status = PaymentStatus.READY,
                )
            val order =
                orderRepository.save(
                    OrdersEntity(
                        productName = findProduct.title,
                        totalPrice = findProduct.price * quantity,
                        address = address,
                        discount = 0.0,
                        product = findProduct,
                        quantity = quantity,
                        socialUser = findUser,
                        orderUid = UUID.randomUUID().toString(),
                        payment = payment,
                        phoneNumber = phoneNumber,
                    ),
                )
            paymentRepository.save(payment)
            productStockRepository.save(stockCheck.discount(quantity))
            return order.toResponse()
        }
    }

    override fun saveToRedis(
        productId: String,
        userId: String,
        orderId: String,
    ) {
        val hashKey: String = orderId

        redisTemplate.opsForHash<String, String>().put(hashKey, "productId", productId)
        redisTemplate.opsForHash<String, String>().put(hashKey, "userId", userId)
        redisTemplate.opsForHash<String, String>().put(hashKey, "orderId", orderId)
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
        // 이미 취소된 주문을 또 찾으면 안되기 때문에 논리삭제가 된 것은 찾지 않도록 함
        if (findUser.id != findOrder.socialUser.id) {
            throw Exception("주문정보가 일치하지 않습니다")
        }

        return if (findOrder.status != OrdersStatus.CANCELLED) {
            findOrder.deletedAt = LocalDateTime.now()
            findOrder.status = OrdersStatus.CANCELLED
            orderRepository.save(findOrder)
            CancelResponseDto(
                message = "주문이 취소 되었습니다",
            )
        } else {
            throw Exception("이미 취소된 주문입니다")
        }
    }

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
        val findProductList = productRepository.findBySellerId(findSeller)
        if (findProductList.isEmpty()) {
            throw ModelNotFoundException("product", sellerId)
        }
        val findOrder = orderRepository.findByIdAndDeletedAtIsNull(orderId).orElseThrow { Exception("존재하지 않는 주문입니다") }
        // 이건 상태를 변경하는거고, 취소된 주문은 이미 상태가 cancelled 이기 때문에, 상태변경을 지원하지 않음.
        val findResult = findProductList.find { it.id == findOrder.product.id }

        return if (findResult != null) {
            findOrder.status = status
            orderRepository.save(findOrder).toResponse()
        } else {
            throw Exception("변경 권한이 없습니다")
        }
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
