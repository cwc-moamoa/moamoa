package com.teamsparta.moamoa.domain.product.dto

import com.teamsparta.moamoa.domain.product.model.Product
import java.time.LocalDateTime

data class ProductResponse(
    val id: Long?,
    val title: String,
    val content: String,
    val imageUrl: String,
    val createdAt: LocalDateTime,
    val price: Int,
    val productDiscount: Double,
    val purchaseAble: Boolean,
    val ratingAverage: Double,
    val likes: Int,
//    val isDeleted: Boolean
    var deletedAt: LocalDateTime?,
) {
    constructor(product: Product) : this(
        id = product.id,
        title = product.title,
        content = product.content,
        imageUrl = product.imageUrl,
        createdAt = product.createdAt,
        price = product.price,
        productDiscount = product.productDiscount,
        purchaseAble = product.purchaseAble,
        ratingAverage = product.ratingAverage,
        likes = product.likes,
//        isDeleted = product.isDeleted
        deletedAt = product.deletedAt,
    )
}
