package com.teamsparta.moamoa.domain.groupPurchase.model

import com.teamsparta.moamoa.infra.BaseTimeEntity
import jakarta.persistence.*

@Table(name = "group_purchases_users")
@Entity
class GroupPurchaseJoinUserEntity(
    @Column(name = "user_id")
    val userId: Long,
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_purchase_id")
    val groupPurchase: GroupPurchaseEntity,
    @Column(name = "order_id")
    val orderId: Long,
) : BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}
