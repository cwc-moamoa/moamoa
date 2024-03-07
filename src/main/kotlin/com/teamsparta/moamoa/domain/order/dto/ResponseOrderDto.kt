package com.teamsparta.moamoa.domain.order.dto

import java.time.LocalDateTime

data class ResponseOrderDto(
    val orderId: Long,
    val productName: String,
    val totalPrice: Int,
    val address: String,
    val createdAt: LocalDateTime,
    val status: String,
    val discount: Double,
    val quantity: Int,
)
