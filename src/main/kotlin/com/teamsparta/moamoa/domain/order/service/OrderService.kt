package com.teamsparta.moamoa.domain.order.service

import com.teamsparta.moamoa.domain.order.dto.*
import com.teamsparta.moamoa.domain.order.model.OrdersStatus
import com.teamsparta.moamoa.infra.security.UserPrincipal
import org.springframework.data.domain.Page

interface OrderService {
    fun createOrder(
        user: UserPrincipal,
        createOrderDto: CreateOrderDto
    ): ResponseOrderDto

    fun createOrderNoLock(
        userId: Long,
        productId: Long,
        quantity: Int,
        address: String,
        phoneNumber: String,
    ): ResponseOrderDto

    fun createGroupOrder(
        user: UserPrincipal,
       createOrderDto: CreateOrderDto
    ): ResponseOrderDto

    fun updateOrder(
        user: UserPrincipal,
        orderId: Long,
        updateOrderDto: UpdateOrderDto,
    ): ResponseOrderDto

    fun cancelOrder(
        user: UserPrincipal,
        orderId: Long,
    ): CancelResponseDto

    fun getOrder(
        user: UserPrincipal,
        orderId: Long,
    ): ResponseOrderDto


    fun getOrderList(
    user: UserPrincipal,
    ) : List<ResponseOrderDto>


    fun getOrderPage(
        userId: Long,
        page: Int,
        size: Int,
    ): Page<ResponseOrderDto>

    fun orderStatusChange(
        orderId: Long,
        sellerId: Long,
        status: OrdersStatus,
    ): ResponseOrderDto

    fun getOrderBySellerId(
        sellerId: Long,
        orderId: Long,
    ): ResponseOrderDto

    fun getOrderPageBySellerId(
        sellerId: Long,
        page: Int,
        size: Int,
    ): Page<ResponseOrderDto>

    fun trollOrderDelete(orderUId: String)

    fun getOrderByOrderUid(
                           orderUId: String,
    ): ResponseOrderDto

    fun createOrderWithLock(
        userId: Long,
        productId: Long,
        quantity: Int,
        address: String,
        phoneNumber: String,
    ): ResponseOrderDto
}
