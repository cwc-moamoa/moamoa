package com.teamsparta.moamoa.review.model

import com.teamsparta.moamoa.infra.BaseTimeEntity
import com.teamsparta.moamoa.product.model.Product
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class Review(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    val product: Product,
    @Column(name = "title")
    var title: String,
    @Column(name = "content")
    var content: String,
    @Column(name = "name")
    var name: String,
    @Column(name = "image_url")
    var imageUrl: String? = null,
    @Column(name = "deleted_at")
    var deletedAt: LocalDateTime? = null,
    @Column(name = "likesCount")
    var likesCount: Int = 0 ,
) : BaseTimeEntity()
