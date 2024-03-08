package com.teamsparta.moamoa.domain.order.service

import com.teamsparta.moamoa.domain.order.dto.*
import com.teamsparta.moamoa.domain.order.model.OrdersEntity
import com.teamsparta.moamoa.domain.order.model.OrdersStatus
import com.teamsparta.moamoa.domain.order.model.toResponse
import com.teamsparta.moamoa.domain.order.repository.OrderRepository
import com.teamsparta.moamoa.domain.product.model.StockEntity.Companion.discount
import com.teamsparta.moamoa.domain.product.repository.ProductRepository
import com.teamsparta.moamoa.domain.product.repository.StockRepository
import com.teamsparta.moamoa.domain.seller.repository.SellerRepository
import com.teamsparta.moamoa.domain.user.repository.UserRepository
import com.teamsparta.moamoa.exception.ModelNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class OrderServiceImpl(
    private val orderRepository: OrderRepository,
    private val productRepository: ProductRepository,
    private val stockRepository: StockRepository,
    private val userRepository: UserRepository,
    private val sellerRepository: SellerRepository,
    private val redisTemplate: RedisTemplate<String, Any>
) : OrderService {
    @Transactional
    override fun creatOrder(
        userId: Long,
        productId: Long,
        createOrderDto: CreateOrderDto,
    ): ResponseOrderDto {
        val findUser = userRepository.findByIdOrNull(userId) ?: throw ModelNotFoundException("user", userId)
        val findProduct =
            productRepository.findByIdOrNull(productId) ?: throw ModelNotFoundException("product", productId)
        val stockCheck = stockRepository.findByProduct(findProduct) ?: throw Exception("없어~")

        return if (stockCheck.stock > 0 && stockCheck.stock > createOrderDto.quantity) {
            stockRepository.save(stockCheck.discount(createOrderDto.quantity)) // 재고 날리고 저장
            orderRepository.save(
                OrdersEntity(
                    productName = findProduct.title,
                    totalPrice = findProduct.price * createOrderDto.quantity,
                    address = createOrderDto.address,
                    discount = 0.0,
                    product = findProduct,
                    quantity = createOrderDto.quantity,
                    user = findUser,
                ),
            ).toResponse()
        } else {
            throw Exception("재고가 모자랍니다 다시 시도")
        }
    }
    //이 로직이 끝날을때, saveToRedis를 실행해서, 지금 만들어서 response된 orderid + 여기 해당하는 productId와 userId를 같이 저장하는게 목표.
    //아래 saveToRedis가 직접 값을 입력하는게 아닌, 값을 받아오기.

    override fun saveToRedis(
        productId: String,
        userId: String,
        orderId: String,
    )  {
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
        val findUser = userRepository.findByIdOrNull(userId) ?: throw ModelNotFoundException("user", userId)
        val findOrders = orderRepository.findByIdOrNull(orderId) ?: throw ModelNotFoundException("orders", orderId)
        if (findOrders.user == findUser) {
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
        val findUser = userRepository.findByIdOrNull(userId) ?: throw ModelNotFoundException("user", userId)
        val findOrder = orderRepository.findByIdOrNull(orderId) ?: throw ModelNotFoundException("orders", orderId)

        if (findUser.id != findOrder.user.id) {
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
        val findUser = userRepository.findByIdOrNull(userId) ?: throw ModelNotFoundException("user", userId)
        val findOrder = orderRepository.findByIdOrNull(orderId) ?: throw ModelNotFoundException("orders", orderId)

        return if (findOrder.user == findUser) {
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
        val findProductList =
            productRepository.findBySeller(findSeller)
        if (findProductList.isEmpty()) {
            throw ModelNotFoundException("product", sellerId)
        }
        val findOrder = orderRepository.findByIdOrNull(orderId) ?: throw ModelNotFoundException("order", orderId)
        val findResult = findProductList.find { it?.productId == findOrder.product.productId }

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

        return if (findOrder.product.seller == findSeller) {
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
