package com.teamsparta.moamoa.domain.order.service

import com.teamsparta.moamoa.domain.order.dto.*
import com.teamsparta.moamoa.domain.order.model.OrdersStatus
import org.springframework.data.domain.Page

interface OrderService {
    fun createOrder(
        userId: Long,
        productId: Long,
        quantity: Int,
        address: String,
        phoneNumber: String,
    ): ResponseOrderDto

    //테스트용 락 없는 코드
    fun createOrderNoLock(
        userId: Long,
        productId: Long,
        quantity: Int,
        address: String,
        phoneNumber: String,
    ): ResponseOrderDto

    fun createGroupOrder(
        userId: Long,
        productId: Long,
        quantity: Int,
        address: String,
        phoneNumber: String,
    ): ResponseOrderDto

    // 주문 생성
    fun updateOrder(
        userId: Long,
        orderId: Long,
        updateOrderDto: UpdateOrderDto,
    ): ResponseOrderDto

    // 주문 업데이트 / 유저
    fun cancelOrder(
        userId: Long,
        orderId: Long,
    ): CancelResponseDto

    // 주문취소 / 유저
    fun getOrder(
        userId: Long,
        orderId: Long,
    ): ResponseOrderDto

    // 주문 단건조회
    fun getOrderPage(
        userId: Long,
        page: Int,
        size: Int,
    ): Page<ResponseOrderDto>

    // 주문 페이지 조회
    fun orderStatusChange(
        orderId: Long,
        sellerId: Long,
        status: OrdersStatus,
    ): ResponseOrderDto

    // 판매자 주문 상태값 변경
    fun getOrderBySellerId(
        sellerId: Long,
        orderId: Long,
    ): ResponseOrderDto

    // 판매자 주문 단건 조회
    fun getOrderPageBySellerId(
        sellerId: Long,
        page: Int,
        size: Int,
    ): Page<ResponseOrderDto>

}
