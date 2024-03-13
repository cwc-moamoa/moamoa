package com.teamsparta.moamoa.domain.payment.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class PaymentCallbackRequest(
    @JsonProperty("payment_uid")
    val paymentUid: String,
    @JsonProperty("order_uid")
    val orderUid: String,
)
