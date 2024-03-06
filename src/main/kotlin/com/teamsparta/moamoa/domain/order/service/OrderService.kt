package com.teamsparta.moamoa.domain.order.service

import com.teamsparta.moamoa.domain.order.dto.*
import com.teamsparta.moamoa.domain.order.model.OrdersStatus
import org.springframework.data.domain.Page

interface OrderService {
    fun creatOrder(
        userId: Long,
        productId: Long,
        createOrderDto: CreateOrderDto,
    ): ResponseOrderDto

    // 주문 생성
    fun updateOrder(
        userId: Long,
        ordersId: Long,
        updateOrderDto: UpdateOrderDto,
    ): ResponseOrderDto

    // 주문 업데이트 / 유저
    fun cancelOrder(
        userId: Long,
        ordersId: Long,
    ): CancelResponseDto

    // 주문취소 / 유저
    fun getOrder(
        userId: Long,
        ordersId: Long,
    ): ResponseOrderDto

    // 주문 단건조회
    fun getOrderPage(
        userId: Long,
        page: Int,
        size: Int,
    ): Page<ResponseOrderDto>

    // 주문 페이지 조회
    fun orderStatusChange(
        ordersId: Long,
        sellerId: Long,
        status: OrdersStatus,
    ): ResponseOrderDto

    // 판매자 주문 상태값 변경
    fun getOrderBySellerId(
        sellerId: Long,
        ordersId: Long,
    ): ResponseOrderDto
}
