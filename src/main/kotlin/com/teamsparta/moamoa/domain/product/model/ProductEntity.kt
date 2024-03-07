package com.teamsparta.moamoa.domain.product.model

import com.teamsparta.moamoa.domain.seller.model.SellerEntity
import jakarta.persistence.*

@Entity
@Table(name = "product")
class ProductEntity(
    @Column(name = "price")
    var price: Int,
    @Column(name = "title")
    var title: String,
    @Column(name = "content")
    var content: String,
    @Column(name = "rating_average")
    var ratingAverage: Double,
    @Column(name = "image_url")
    var imageUrl: String,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var productId: Long? = null

    @Column(name = "purchase_able")
    var purchaseAble: Boolean = false

    // 일단은 빼놓으셈
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    var seller: SellerEntity? = null
}
