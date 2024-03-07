package com.teamsparta.moamoa.review.dto

import com.teamsparta.moamoa.product.model.Product
import com.teamsparta.moamoa.review.model.Review

data class CreateReviewRequest(
    val title: String,
    val content: String,
    val name: String,
    val imageUrl: String,
) {
    fun toReview(product: Product): Review {
        return Review(
            title = title,
            content = content,
            name = name,
            imageUrl = imageUrl,
            product = product,
        )
    }
}
