package com.teamsparta.moamoa.domain.like.model

import com.teamsparta.moamoa.domain.product.model.Product
import com.teamsparta.moamoa.domain.review.model.Review
import com.teamsparta.moamoa.domain.socialUser.model.SocialUser
import com.teamsparta.moamoa.infra.BaseTimeEntity
import jakarta.persistence.*

@Entity
@Table(name = "product_review_like")
class Like(
    @Id @GeneratedValue
    val id: Long? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    val product: Product? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    val review: Review? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "social_user_id")
    val socialUser: SocialUser,
    @Column(name = "status")
    var status: Boolean = false,
) : BaseTimeEntity()
