package com.teamsparta.moamoa.domain.review.model

import com.teamsparta.moamoa.domain.product.model.Product
import com.teamsparta.moamoa.infra.BaseTimeEntity
import jakarta.persistence.*

@Entity
class Review(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
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
) : BaseTimeEntity()
