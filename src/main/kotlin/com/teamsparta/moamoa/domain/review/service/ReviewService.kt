package com.teamsparta.moamoa.domain.review.service

import com.teamsparta.moamoa.domain.review.dto.CreateReviewRequest
import com.teamsparta.moamoa.domain.review.dto.ReviewResponse
import com.teamsparta.moamoa.domain.review.dto.ReviewResponseByList
import com.teamsparta.moamoa.domain.review.dto.UpdateReviewRequest
import com.teamsparta.moamoa.infra.security.UserPrincipal
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ReviewService {
    fun createReview(
        productId: Long,
        socialUser: UserPrincipal,
        createReviewRequest: CreateReviewRequest,
    ): ReviewResponse

    fun updateReview(
        reviewId: Long,
        socialUser: UserPrincipal,
        request: UpdateReviewRequest,
    ): ReviewResponse

    fun getReviewById(reviewId: Long): ReviewResponse

    fun getPaginatedReviewList(
        productId: Long,
        pageable: Pageable,
    ): Page<ReviewResponse>

    fun deleteReview(
        reviewId: Long,
        socialUser: UserPrincipal,
    )

    fun getReviewsByProductId(
        productId: Long
    ): List<ReviewResponseByList>
}
