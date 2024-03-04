package com.teamsparta.moamoa.review.dto

import com.teamsparta.moamoa.review.model.Product
import com.teamsparta.moamoa.review.model.Review

data class CreateReviewRequest (
    val title: String,
    val content: String,
    val name: String,
    val imagePath: String
) {
    fun toReview(product: Product): Review {
        return Review(
            title = title,
            content = content,
            name = name,
            imagePath = imagePath,
            product = product
        )
    }
}