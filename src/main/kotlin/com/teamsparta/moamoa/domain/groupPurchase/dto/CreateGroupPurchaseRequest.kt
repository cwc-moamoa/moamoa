package com.teamsparta.moamoa.domain.groupPurchase.dto

import java.time.LocalDateTime

data class CreateGroupPurchaseRequest(
    val productId: Int,
    val userLimit: Int,
    val timeLimit: LocalDateTime,
    val discount: Double
)