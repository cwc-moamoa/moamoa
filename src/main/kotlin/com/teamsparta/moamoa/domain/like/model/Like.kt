package com.teamsparta.moamoa.domain.like.model

// import com.teamsparta.moamoa.infra.BaseTimeEntity
// import com.teamsparta.moamoa.domain.product.model.Product
// import com.teamsparta.moamoa.domain.review.model.Review
// import com.teamsparta.moamoa.domain.user.model.User
// import jakarta.persistence.*

// @Entity
// data class Like(
//    @Id @GeneratedValue
//    val id: Long? = null,
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "product_id")
//    val product: Product? = null,
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "review_id")
//    val review: Review? = null,
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    val user: User,
//
//    @Column(name = "status")
//    var status: Boolean
// ): BaseTimeEntity()
