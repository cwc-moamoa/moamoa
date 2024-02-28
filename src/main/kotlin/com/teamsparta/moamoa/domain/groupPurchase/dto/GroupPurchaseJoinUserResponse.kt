package com.teamsparta.moamoa.domain.groupPurchase.dto

data class GroupPurchaseJoinUserResponse(
    val id: Long?,
    val userId: Long,
    val orderId: Long,
    val groupPurchaseId: Long
)