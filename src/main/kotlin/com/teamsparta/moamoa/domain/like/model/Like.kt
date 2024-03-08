package com.teamsparta.moamoa.like.model

 import com.teamsparta.moamoa.infra.BaseTimeEntity
 import com.teamsparta.moamoa.product.model.Product
 import com.teamsparta.moamoa.review.model.Review
 import com.teamsparta.moamoa.user.model.User
 import jakarta.persistence.*

 @Entity
 @Table(name = "product_review_like")
 data class Like(
    @Id @GeneratedValue
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    val product: Product? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    val review: Review? = null,

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    val user: User,

    @Column(name = "status")
    var status: Boolean
 ): BaseTimeEntity()
