package com.teamsparta.moamoa.event

data class DiscountPaymentEvent(
    val userId: String,
    val orderId: String,
)
