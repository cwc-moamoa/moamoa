package com.teamsparta.moamoa.domain.order.service

import com.teamsparta.moamoa.domain.order.dto.CreateOrderDto
import com.teamsparta.moamoa.domain.order.dto.ResponseOrderDto

interface OrderService {

    fun creatOrder(userId:Long,productId:Long,createOrderDto: CreateOrderDto):ResponseOrderDto
}