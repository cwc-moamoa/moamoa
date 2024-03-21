package com.teamsparta.moamoa.domain.order.dto

import com.teamsparta.moamoa.domain.seller.model.Seller
import java.time.LocalDateTime

data class ResponseOrderDto(
    val orderId: Long,
    val productName: String,
    val totalPrice: Double,
    val address: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val status: String,
    val discount: Double,
    val quantity: Int,
    val orderUid: String,
)
