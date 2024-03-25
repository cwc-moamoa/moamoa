package com.teamsparta.moamoa.domain.fixture

import com.teamsparta.moamoa.domain.review.model.Review

class CreateReviewFixture {
    companion object {
        fun createReview(
            title : String,
            content: String,
            rating: Int,
            imageUrl: String
        ) : Review {
            return Review(
                id = 1L,
                product = CreateProductFixture.createProduct(),
                socialUser = CreateSocialUserFixture.createSocialUser(),
                content = content,
                rating = rating,
                title = title,
                imageUrl = imageUrl
            )
        }
    }
}