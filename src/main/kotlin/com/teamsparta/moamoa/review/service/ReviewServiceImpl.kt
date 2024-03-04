package com.teamsparta.moamoa.review.service

import com.teamsparta.moamoa.exception.ModelNotFoundException
import com.teamsparta.moamoa.product.repository.ProductRepository
import com.teamsparta.moamoa.review.dto.CreateReviewRequest
import com.teamsparta.moamoa.review.dto.ReviewResponse
import com.teamsparta.moamoa.review.dto.UpdateReviewRequest
import com.teamsparta.moamoa.review.repository.ReviewRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime


@Service
class ReviewServiceImpl(
    private val reviewRepository: ReviewRepository,
    private val productRepository: ProductRepository
): ReviewService {

    @Transactional
    override fun createReview(productId: Long, createReviewRequest: CreateReviewRequest
    ): ReviewResponse {
        val product = productRepository.findByIdOrNull(productId)
            ?: throw ModelNotFoundException("Product", productId)

        val review = createReviewRequest.toReview(product)

        val savedReview = reviewRepository.save(review)
        return ReviewResponse.toReviewResponse(savedReview)
    }

    @Transactional
    override fun updateReview(reviewId: Long, request: UpdateReviewRequest
    ): ReviewResponse {
        val review = reviewRepository.findByIdOrNull(reviewId)
            ?: throw ModelNotFoundException("Review", reviewId)

        review.content = request.content

        return ReviewResponse.toReviewResponse(review)
    }

    @Transactional
    override fun deleteReview(reviewId: Long) {
        val review = reviewRepository.findByIdOrNull(reviewId)
            ?: throw ModelNotFoundException("Review", reviewId)

        review.deletedAt = LocalDateTime.now()
        reviewRepository.save(review)
    }

}