package com.teamsparta.moamoa.domain.product.dto

data class ProductRequest(
    val title: String,
    val content: String,
    val imageUrl: String,
    val price: Int,
    val purchaseAble: Boolean,
    val likes: Int,
    val productDiscount: Double,
    val ratingAverage: Double,
//    val deletedAt: LocalDateTime?, 이거 있으면 안됨
    val userLimit: Int,
    val discount: Double,
    val stock: Int, // 재고?
)
