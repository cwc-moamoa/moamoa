package com.teamsparta.moamoa.domain.groupPurchase.repository

import com.teamsparta.moamoa.domain.groupPurchase.model.GroupPurchaseJoinUserEntity
import io.lettuce.core.dynamic.annotation.Param
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface GroupPurchaseJoinUserRepository : JpaRepository<GroupPurchaseJoinUserEntity, Long>,CustomGroupPurchaseJoinUserRepository {
    fun findByUserIdAndGroupPurchaseId(
        userId: Long,
        groupPurchaseId: Long,
    ): GroupPurchaseJoinUserEntity?

//    fun findByGroupPurchaseId(purchaseId: Long): List<GroupPurchaseJoinUserEntity>

//    @Modifying(clearAutomatically = true)
//    @Query("UPDATE GroupPurchaseJoinUserEntity u SET u.deletedAt = CURRENT_TIMESTAMP WHERE u.groupPurchase.id = :groupPurchaseId")
//    fun softDeleteByGroupPurchaseId(@Param("groupPurchaseId") groupPurchaseId: Long)

    fun findByOrderId(orderId: Long): GroupPurchaseJoinUserEntity?
}
