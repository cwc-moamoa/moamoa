package com.teamsparta.moamoa.review.dto

import com.teamsparta.moamoa.exception.ModelNotFoundException
import com.teamsparta.moamoa.review.model.Review

class ReviewResponse(
    var id: Long?,
    var title: String,
    var content: String,
    var imageUrl: String,
    var name: String,
    var productId: Long


) {

    companion object {
        fun toReviewResponse(review: Review): ReviewResponse {
            val productId = review.product?.id ?: -1

            return ReviewResponse(
                id = review.id,
                title = review.title,
                content = review.content,
                imageUrl = review.imagePath ?: "",
                name = review.name,
                productId = productId
            )
        }
    }
}