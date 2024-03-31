package com.teamsparta.moamoa.domain.order.service

import com.teamsparta.moamoa.domain.order.dto.*
import com.teamsparta.moamoa.domain.order.model.OrdersStatus
import com.teamsparta.moamoa.infra.security.UserPrincipal
import org.springframework.data.domain.Page

interface OrderService {
    fun createOrder(
        user: UserPrincipal,
        productId: Long,
        quantity: Int,
        address: String,
        phoneNumber: String,
    ): ResponseOrderDto

//    fun createOrder(
//        userId: Long,
//        productId: Long,
//        quantity: Int,
//        address: String,
//        phoneNumber: String,
//    ): ResponseOrderDto

    // 테스트용 락 없는 코드
    fun createOrderNoLock(
        userId: Long,
        productId: Long,
        quantity: Int,
        address: String,
        phoneNumber: String,
    ): ResponseOrderDto

    fun createGroupOrder(
        user: UserPrincipal,
        productId: Long,
        quantity: Int,
        address: String,
        phoneNumber: String,
    ): ResponseOrderDto

    // 주문 생성
    fun updateOrder(
        user: UserPrincipal,
        orderId: Long,
        updateOrderDto: UpdateOrderDto,
    ): ResponseOrderDto

    // 주문 업데이트 / 유저
    fun cancelOrder(
        user: UserPrincipal,
        orderId: Long,
    ): CancelResponseDto

    // 주문취소 / 유저
    fun getOrder(
        user: UserPrincipal,
        orderId: Long,
    ): ResponseOrderDto

    // 주문 단건조회

    fun getOrderList(
    user: UserPrincipal,
    ) : List<ResponseOrderDto>

    //주문 리스트 조회

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

    fun trollOrderDelete(orderUId: String)

    fun getOrderByOrderUid(
                           orderUId: String,
    ): ResponseOrderDto

}
