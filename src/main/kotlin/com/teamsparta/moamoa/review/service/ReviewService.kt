package com.teamsparta.moamoa.review.service

import com.teamsparta.moamoa.review.dto.CreateReviewRequest
import com.teamsparta.moamoa.review.dto.ReviewResponse
import com.teamsparta.moamoa.review.dto.UpdateReviewRequest

interface ReviewService {

    fun createReview (productId:Long, createReviewRequest: CreateReviewRequest): ReviewResponse
    fun updateReview (reviewId: Long, request: UpdateReviewRequest): ReviewResponse
    fun deleteReview (reviewId: Long)

}