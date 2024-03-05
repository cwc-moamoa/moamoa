package com.teamsparta.moamoa.domain.order.service

import com.teamsparta.moamoa.domain.order.dto.CancelResponseDto
import com.teamsparta.moamoa.domain.order.dto.CreateOrderDto
import com.teamsparta.moamoa.domain.order.dto.ResponseOrderDto
import com.teamsparta.moamoa.domain.order.dto.UpdateOrderDto

interface OrderService {

    fun creatOrder(userId:Long,productId:Long,createOrderDto: CreateOrderDto):ResponseOrderDto

    fun updateOrder(userId: Long,ordersId:Long,updateOrderDto: UpdateOrderDto):ResponseOrderDto

    fun cancelOrder(userId: Long,ordersId: Long):CancelResponseDto

    fun getOrder(userId: Long,ordersId: Long):ResponseOrderDto
}