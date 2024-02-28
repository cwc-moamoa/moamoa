package com.teamsparta.moamoa.domain.groupPurchase.dto

import java.time.LocalDateTime

data class GroupPurchaseResponse(
    val id: Long?,
    val productId: Int,
    val userLimit: Int,
    val userCount: Int,
    val timeLimit: LocalDateTime,
    val discount: Double
)