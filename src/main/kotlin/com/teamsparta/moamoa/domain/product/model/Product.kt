package com.teamsparta.moamoa.domain.product.model

import com.fasterxml.jackson.annotation.JsonManagedReference
import com.teamsparta.moamoa.domain.seller.model.Seller
import com.teamsparta.moamoa.infra.BaseTimeEntity
import jakarta.persistence.*

@Entity
@Table(name = "product")
data class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sellerId", nullable = false)
    var seller: Seller,
    @Column(name = "price", nullable = false)
    var price: Double,
    @Column(name = "title", nullable = false)
    var title: String,
    @Column(name = "content", nullable = false)
    var content: String,
    @Column(name = "purchaseAble", nullable = false)
    var purchaseAble: Boolean,
    @Column(name = "ratingAverage", nullable = true)
    val ratingAverage: Double,
    @Column(name = "imageUrl", nullable = true)
    var imageUrl: String,
    @Column(name = "likes", nullable = true)
    val likes: Int,
    @Column(name = "userLimit")
    val userLimit: Int,
    @Column(name = "discount")
    val discount: Double,
    @Column(name = "is_sold_out", nullable = false)
    var isSoldOut: Boolean = false,
) : BaseTimeEntity()
