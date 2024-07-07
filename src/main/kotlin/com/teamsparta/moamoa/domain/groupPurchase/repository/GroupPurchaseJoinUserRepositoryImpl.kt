package com.teamsparta.moamoa.domain.groupPurchase.repository

import com.teamsparta.moamoa.domain.groupPurchase.model.QGroupPurchaseJoinUserEntity
import com.teamsparta.moamoa.infra.QueryDslSupport

class GroupPurchaseJoinUserRepositoryImpl : CustomGroupPurchaseJoinUserRepository,QueryDslSupport() {
    private val groupPurchaseJoinUserEntity = QGroupPurchaseJoinUserEntity.groupPurchaseJoinUserEntity

    override fun findByGroupPurchaseIdAndSoftDelete(purchaseId: Long) {
        queryFactory.update(groupPurchaseJoinUserEntity)
            .where(groupPurchaseJoinUserEntity.groupPurchase.id.eq(purchaseId))
            .set(groupPurchaseJoinUserEntity.deletedAt, java.time.LocalDateTime.now())
            .execute()

            entityManager.flush()
            entityManager.clear()

    }
}