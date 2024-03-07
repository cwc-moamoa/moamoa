package com.teamsparta.moamoa.product.dto

data class ProductRequest(
    val title: String,
    val content: String,
    val imageUrl: String,
    val price: Int,
    val purchaseAble: Boolean,
    val likes: Int,
    val productDiscount: Double,
    val ratingAverage: Double,
    val sellerId: Long,
//    val deletedAt: LocalDateTime?, 이거 있으면 안됨
    val userLimit: Int, // 추가된 필드
    val discount: Double, // 추가된 필드
)
