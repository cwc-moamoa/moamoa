package com.teamsparta.moamoa.domain.order.repository

import com.teamsparta.moamoa.domain.order.model.OrdersEntity
import org.springframework.data.domain.Page

interface CustomOrderRepository {

    fun getOrderPage(userId:Long,page:Int,size:Int):Page<OrdersEntity>


}