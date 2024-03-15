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
//    @Transactional
//    override fun createOrder(
//        userId: Long,
//        productId: Long,
//        quantity: Int,
//        address: String,
//    ): ResponseOrderDto {
//        val findUser = userRepository.findByIdOrNull(userId) ?: throw ModelNotFoundException("user", userId)
//        val findProduct =
//            productRepository.findByIdOrNull(productId) ?: throw ModelNotFoundException("product", productId)
//        val stockCheck = productStockRepository.findByProduct(findProduct) ?: throw Exception("없어~")
//
//        return if (stockCheck.stock > 0 && stockCheck.stock >= quantity) {
//            val order = orderRepository.save(
//                OrdersEntity(
//                    productName = findProduct.title,
//                    totalPrice = findProduct.price * quantity,
//                    address = address,
//                    discount = findProduct.discount,
//                    product = findProduct,
//                    quantity = quantity,
//                    user = findUser,
//                )
//            )
//
//            productStockRepository.save(stockCheck.discount(quantity))
//
//            order.toResponse()
//        } else {
//            throw Exception("재고가 모자랍니다 다시 시도")
//        }
//    }

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
            productRepository.findByIdOrNull(productId) ?: throw Exception("존재하지 않는 상풉입니다")
        val stockCheck = productStockRepository.findByProduct(findProduct)

        if (stockCheck!!.stock < 0 && stockCheck.stock <= quantity) throw Exception("재고가 모자랍니다. 판매자에게 문의해주세요")

        // 어떻게 막을지 생각하기....
        if (findProduct.discount > 0) {
            val groupPurchaseCheck = groupPurchaseRepository.findByIdOrNull(findProduct.id)
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
        val findOrders = orderRepository.findByIdOrNull(orderId) ?: throw ModelNotFoundException("orders", orderId)
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
        val findOrder = orderRepository.findByIdOrNull(orderId) ?: throw ModelNotFoundException("orders", orderId)

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
        // 업데이트 시간 디테일 너무 투마치 ㅋㅋ
    }

    override fun getOrder(
        userId: Long,
        orderId: Long,
    ): ResponseOrderDto {
        val findUser = socialUserRepository.findByIdOrNull(userId) ?: throw ModelNotFoundException("user", userId)
        val findOrder = orderRepository.findByIdOrNull(orderId) ?: throw ModelNotFoundException("orders", orderId)

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
        val findSeller = sellerRepository.findByIdOrNull(sellerId) ?: throw ModelNotFoundException("seller", sellerId)
        val findProductList = productRepository.findBySellerId(findSeller)
        if (findProductList.isEmpty()) {
            throw ModelNotFoundException("product", sellerId)
        }
        val findOrder = orderRepository.findByIdOrNull(orderId) ?: throw ModelNotFoundException("order", orderId)
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
        val findSeller = sellerRepository.findByIdOrNull(sellerId) ?: throw ModelNotFoundException("seller", sellerId)
        val findOrder = orderRepository.findByIdOrNull(orderId) ?: throw ModelNotFoundException("order", orderId)

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
    }
}
