package com.teamsparta.moamoa.domain.groupPurchase.repository


interface CustomGroupPurchaseJoinUserRepository{
    fun findByGroupPurchaseIdAndSoftDelete(purchaseId: Long)
}
