package com.teamsparta.moamoa.domain.groupPurchase.repository

import com.teamsparta.moamoa.domain.groupPurchase.model.GroupPurchaseEntity
import org.springframework.data.jpa.repository.JpaRepository

interface GroupPurchaseRepository : JpaRepository<GroupPurchaseEntity, Long> {
    fun findByProductIdAndDeletedAtIsNull(productId: Long): GroupPurchaseEntity?

    fun findByIdAndDeletedAtIsNull(groupPurchaseId: Long): GroupPurchaseEntity?
}
