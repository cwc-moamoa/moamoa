package com.teamsparta.moamoa.product.dto

import java.time.LocalDateTime

data class ProductRequest(
    val title: String,
    val content: String,
    val imageUrl: String,
    val price: Int,
    val purchaseAble: Boolean,
//    val isDeleted: Boolean,
    val likes: Int,
    val productDiscount: Double,
    val ratingAverage: Double,
    val sellerId: Long,
    val deletedAt: LocalDateTime? = null,
    val userLimit: Int, // 추가된 필드
    val discount: Double // 추가된 필드
)
