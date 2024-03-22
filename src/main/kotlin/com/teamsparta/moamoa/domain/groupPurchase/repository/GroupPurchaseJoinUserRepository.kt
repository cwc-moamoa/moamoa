package com.teamsparta.moamoa.domain.groupPurchase.repository

import com.teamsparta.moamoa.domain.groupPurchase.model.GroupPurchaseJoinUserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface GroupPurchaseJoinUserRepository : JpaRepository<GroupPurchaseJoinUserEntity, Long> {
    fun findByUserIdAndGroupPurchaseId(
        userId: Long,
        groupPurchaseId: Long,
    ): GroupPurchaseJoinUserEntity?

    fun findByGroupPurchaseId(purchaseId: Long): List<GroupPurchaseJoinUserEntity>

    fun findByOrderId(orderId:Long):GroupPurchaseJoinUserEntity?
}
