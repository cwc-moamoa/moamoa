package com.teamsparta.moamoa.domain.product.model

import com.fasterxml.jackson.annotation.JsonManagedReference
import com.teamsparta.moamoa.domain.seller.model.SellerEntity
import com.teamsparta.moamoa.infra.BaseTimeEntity
import jakarta.persistence.*

@Entity
@Table(name = "product")
data class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sellerId", nullable = false)
    var seller: SellerEntity,
    @Column(name = "price", nullable = false)
    var price: Int,
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
    @Column(name = "productDiscount", nullable = true)
    val productDiscount: Double,
    //    @Column(name = "likes", nullable = true)
//    val likes: Int,
    @Column(name = "likes")
    var likes: Int = 0,
    @Column(name = "userLimit")
    val userLimit: Int,
    @Column(name = "discount")
    val discount: Double,
    @OneToOne(mappedBy = "product", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JsonManagedReference
    var productStock: ProductStock? = null,
    @Column(name = "is_sold_out", nullable = false)
    var isSoldOut: Boolean = false,
) : BaseTimeEntity()
