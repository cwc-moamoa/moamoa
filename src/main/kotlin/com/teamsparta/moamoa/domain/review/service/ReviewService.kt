package com.teamsparta.moamoa.domain.review.service

import com.teamsparta.moamoa.domain.review.dto.CreateReviewRequest
import com.teamsparta.moamoa.domain.review.dto.ReviewResponse
import com.teamsparta.moamoa.domain.review.dto.UpdateReviewRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ReviewService {
    fun createReview(productId: Long, createReviewRequest: CreateReviewRequest, ): ReviewResponse

    fun updateReview(reviewId: Long, request: UpdateReviewRequest): ReviewResponse

    fun getReviewById(reviewId: Long): ReviewResponse

    fun getPaginatedReviewList(productId: Long, pageable: Pageable): Page<ReviewResponse>

    fun deleteReview(reviewId: Long)
}
