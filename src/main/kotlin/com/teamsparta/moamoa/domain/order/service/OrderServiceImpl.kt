package com.teamsparta.moamoa.domain.order.service

import com.teamsparta.moamoa.domain.order.dto.CreateOrderDto
import com.teamsparta.moamoa.domain.order.dto.ResponseOrderDto
import com.teamsparta.moamoa.domain.order.dto.UpdateOrderDto
import com.teamsparta.moamoa.domain.order.model.OrdersEntity
import com.teamsparta.moamoa.domain.order.model.toResponse
import com.teamsparta.moamoa.domain.order.repository.OrderRepository
import com.teamsparta.moamoa.domain.product.model.StockEntity.Companion.discount
import com.teamsparta.moamoa.domain.product.repository.ProductRepository
import com.teamsparta.moamoa.domain.product.repository.StockRepository
import com.teamsparta.moamoa.domain.user.repository.UserRepository
import com.teamsparta.moamoa.exception.ModelNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class OrderServiceImpl(
    private val orderRepository: OrderRepository,
    private val productRepository: ProductRepository,
    private val stockRepository: StockRepository,
    private val userRepository: UserRepository
):OrderService {
    @Transactional
    override fun creatOrder(userId: Long, productId: Long, createOrderDto: CreateOrderDto): ResponseOrderDto {
        val findUser = userRepository.findByIdOrNull(userId) ?: throw ModelNotFoundException("user", userId)
        val findProduct =
            productRepository.findByIdOrNull(productId) ?: throw ModelNotFoundException("product", productId)
        val stockCheck = stockRepository.findByProductId(findProduct) ?: throw Exception("없어~")

        return if (stockCheck.stock > 0 && stockCheck.stock > createOrderDto.quantity) {
            stockRepository.save(stockCheck.discount(createOrderDto.quantity))// 재고 날리고 저장
            orderRepository.save(
                OrdersEntity(
                    productName = findProduct.title,
                    totalPrice = findProduct.price * createOrderDto.quantity,
                    address = createOrderDto.address,
                    createdAt = LocalDateTime.now(),
                    discount = 0.0,
                    productId = findProduct,
                    quantity = createOrderDto.quantity,
                    userId = findUser
                )
            ).toResponse()
        } else {
            throw Exception("재고가 모자랍니다 다시 시도")
        }

    }

    @Transactional
    override fun updateOrder( userId: Long,ordersId: Long,updateOrderDto: UpdateOrderDto): ResponseOrderDto {
        val findUser = userRepository.findByIdOrNull(userId)?: throw ModelNotFoundException("user",userId)
        val findOrders = orderRepository.findByIdOrNull(ordersId)?:throw ModelNotFoundException("user",ordersId)
            if(findOrders.userId == findUser){
                findOrders.address = updateOrderDto.address
                return orderRepository.save(findOrders).toResponse()
            }else{
                throw Exception("도둑 검거 완료")
            }

    }
}

