package com.teamsparta.moamoa.domain.payment.dto

data class RequestPayDto(
    val orderUid: String,
    val itemName: String,
    val buyerName: String,
    val paymentPrice: Double,
    val buyerAddress: String,
    val buyerPhone: String,
)
