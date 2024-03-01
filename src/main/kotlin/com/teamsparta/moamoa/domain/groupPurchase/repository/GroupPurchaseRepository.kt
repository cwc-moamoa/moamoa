package com.teamsparta.moamoa.domain.groupPurchase.repository

import com.teamsparta.moamoa.domain.groupPurchase.model.GroupPurchaseEntity
import com.teamsparta.moamoa.domain.groupPurchase.model.GroupPurchaseJoinUserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface GroupPurchaseRepository : JpaRepository<GroupPurchaseEntity, Long> {

    @Query("SELECT g FROM GroupPurchaseEntity g WHERE g.productId = :productId AND g.deletedAt IS NULL")
    fun findByProductId(@Param("productId") productId: Long): GroupPurchaseEntity?


    @Query("SELECT g FROM GroupPurchaseEntity g WHERE g.id = :groupPurchaseId AND g.deletedAt IS NULL")
    fun findByIdOrNull(@Param("groupPurchaseId") groupPurchaseId: Long): GroupPurchaseEntity?

}