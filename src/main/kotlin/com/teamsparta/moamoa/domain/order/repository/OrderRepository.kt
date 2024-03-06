package com.teamsparta.moamoa.domain.order.repository

import com.teamsparta.moamoa.domain.order.model.OrdersEntity
import org.springframework.data.jpa.repository.JpaRepository

interface OrderRepository : JpaRepository<OrdersEntity, Long>, CustomOrderRepository
