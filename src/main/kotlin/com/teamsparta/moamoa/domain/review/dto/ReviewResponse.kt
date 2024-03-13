package com.teamsparta.moamoa.domain.review.dto

import com.teamsparta.moamoa.domain.review.model.Review

class ReviewResponse(
    var id: Long?,
    var title: String,
    var content: String,
    var imageUrl: String,
    var name: String,
) {
    companion object {
        fun toReviewResponse(review: Review): ReviewResponse {
            return ReviewResponse(
                id = review.id,
                title = review.title,
                content = review.content,
                imageUrl = review.imageUrl ?: "",
                name = review.name,
            )
        }
    }
}
