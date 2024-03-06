package com.teamsparta.moamoa.domain.order.service

import com.teamsparta.moamoa.domain.order.dto.*
import com.teamsparta.moamoa.domain.order.model.OrdersEntity
import com.teamsparta.moamoa.domain.order.model.OrdersStatus
import com.teamsparta.moamoa.domain.order.model.toResponse
import com.teamsparta.moamoa.domain.order.repository.OrderRepository
import com.teamsparta.moamoa.domain.product.model.StockEntity.Companion.discount
import com.teamsparta.moamoa.domain.product.repository.ProductRepository
import com.teamsparta.moamoa.domain.product.repository.StockRepository
import com.teamsparta.moamoa.domain.seller.model.SellerRepository
import com.teamsparta.moamoa.domain.user.repository.UserRepository
import com.teamsparta.moamoa.exception.ModelNotFoundException
import org.springframework.data.domain.Page
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
                    createdAt = LocalDateTime.now(),
                    discount = 0.0,
                    product = findProduct,
                    quantity = createOrderDto.quantity,
                    userId = findUser,
                ),
            ).toResponse()
        } else {
            throw Exception("재고가 모자랍니다 다시 시도")
        }
    } // 재고수량 점검 로직 합칠수도있지않나?

    @Transactional
    override fun updateOrder(
        userId: Long,
        ordersId: Long,
        updateOrderDto: UpdateOrderDto,
    ): ResponseOrderDto {
        val findUser = userRepository.findByIdOrNull(userId) ?: throw ModelNotFoundException("user", userId)
        val findOrders = orderRepository.findByIdOrNull(ordersId) ?: throw ModelNotFoundException("orders", ordersId)
        if (findOrders.userId == findUser) {
            findOrders.address = updateOrderDto.address
            return orderRepository.save(findOrders).toResponse()
        } else {
            throw Exception("도둑 검거 완료")
        }
    }

    @Transactional
    override fun cancelOrder(
        userId: Long,
        ordersId: Long,
    ): CancelResponseDto {
        val findUser = userRepository.findByIdOrNull(userId) ?: throw ModelNotFoundException("user", userId)
        val findOrder = orderRepository.findByIdOrNull(ordersId) ?: throw ModelNotFoundException("orders", ordersId)
        // val findOrderAndUser = orderRepository.findByOrdersIdAndUserId(userId,ordersId)?: throw IllegalStateException("Invalid userId or ordersId")
        if (findUser.id == findOrder.userId.id)
            {
                throw Exception("주문정보가 일치하지 않습니다")
            }

        return if (findOrder.status != OrdersStatus.CANCELLED)
            {
                findOrder.deletedAt = LocalDateTime.now()
                findOrder.status = OrdersStatus.CANCELLED
                orderRepository.save(findOrder)
                CancelResponseDto(
                    message = "주문이 취소 되었습니다",
                )
            } else
            {
                throw Exception("이미 취소된 주문입니다")
            }
    }

    override fun getOrder(
        userId: Long,
        ordersId: Long,
    ): ResponseOrderDto {
        val findUser = userRepository.findByIdOrNull(userId) ?: throw ModelNotFoundException("user", userId)
        val findOrder = orderRepository.findByIdOrNull(ordersId) ?: throw ModelNotFoundException("orders", ordersId)

        return if (findOrder.userId == findUser) {
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
        ordersId: Long,
        sellerId: Long,
        status: OrdersStatus,
    ): ResponseOrderDto {
        val findSeller = sellerRepository.findByIdOrNull(sellerId) ?: throw ModelNotFoundException("seller", sellerId)
        val findProductList =
            productRepository.findBySeller(findSeller)
        if (findProductList.isEmpty()) {
            throw ModelNotFoundException("product", sellerId)
        }
        val findOrder = orderRepository.findByIdOrNull(ordersId) ?: throw ModelNotFoundException("order", ordersId)
        val findResult = findProductList.find { it?.productId == findOrder.product.productId }

        return if (findResult != null) {
            findOrder.status = status
            orderRepository.save(findOrder).toResponse()
        } else {
            throw Exception("변경 권한이 없습니다")
        }
    }
}
