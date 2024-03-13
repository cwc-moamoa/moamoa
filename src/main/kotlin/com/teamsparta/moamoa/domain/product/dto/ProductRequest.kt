package com.teamsparta.moamoa.domain.product.dto

data class ProductRequest(
    val title: String,
    val content: String,
    val imageUrl: String,
    val price: Double,
    val purchaseAble: Boolean,
    val productDiscount: Double,
    val ratingAverage: Double,
    val userLimit: Int,
    val discount: Double,
    val stock: Int, // 재고?
)
