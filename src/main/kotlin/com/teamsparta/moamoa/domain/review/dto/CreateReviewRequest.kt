package com.teamsparta.moamoa.domain.review.dto

import com.teamsparta.moamoa.domain.product.model.Product
import com.teamsparta.moamoa.domain.review.model.Review
import com.teamsparta.moamoa.domain.socialUser.model.SocialUser

data class CreateReviewRequest(
    val title: String,
    val content: String,
    val name: String,
    val imageUrl: String?,
    val rating: Int,
    val socialUserId: Long,
) {
    fun toReview(
        product: Product,
        socialUser: SocialUser,
    ): Review {
        return Review(
            title = title,
            content = content,
            name = name,
            imageUrl = imageUrl,
            product = product,
            likes = 0,
            rating = rating,
            socialUser = socialUser,
        )
    }
}
