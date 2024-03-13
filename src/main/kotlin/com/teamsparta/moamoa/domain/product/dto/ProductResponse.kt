package com.teamsparta.moamoa.domain.product.dto

import com.teamsparta.moamoa.domain.product.model.Product
import java.time.LocalDateTime

data class ProductResponse(
    val id: Long?,
    val title: String,
    val content: String,
    val imageUrl: String,
    val createdAt: LocalDateTime,
    val price: Double,
    val discount: Double,
    val purchaseAble: Boolean,
    val ratingAverage: Double,
    val likes: Int,
    var deletedAt: LocalDateTime?,
) {
    constructor(product: Product) : this(
        id = product.id,
        title = product.title,
        content = product.content,
        imageUrl = product.imageUrl,
        createdAt = product.createdAt,
        price = product.price,
        discount = product.discount,
        purchaseAble = product.purchaseAble,
        ratingAverage = product.ratingAverage,
        likes = product.likes,
        deletedAt = product.deletedAt,
    )
}
