package com.teamsparta.moamoa.domain.review.service

import com.teamsparta.moamoa.domain.product.repository.ProductRepository
import com.teamsparta.moamoa.domain.review.dto.CreateReviewRequest
import com.teamsparta.moamoa.domain.review.dto.ReviewResponse
import com.teamsparta.moamoa.domain.review.dto.UpdateReviewRequest
import com.teamsparta.moamoa.domain.review.repository.ReviewRepository
import com.teamsparta.moamoa.exception.ModelNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class ReviewServiceImpl(
    private val reviewRepository: ReviewRepository,
    private val productRepository: ProductRepository,
) : ReviewService {
    private fun validateRating(rating: Int) {
        if (rating < 1 || rating > 5) {
            throw IllegalArgumentException("Rating must be between 1 and 5.")
        }
    }

    @Transactional
    override fun createReview(
        productId: Long,
        createReviewRequest: CreateReviewRequest,
    ): ReviewResponse {
        validateRating(createReviewRequest.rating)

        val product =
            productRepository.findByIdAndDeletedAtIsNull(productId)
                .orElseThrow { ModelNotFoundException("Product not found or deleted", productId) }

        val review = createReviewRequest.toReview(product)

        val savedReview = reviewRepository.save(review)
        return ReviewResponse.toReviewResponse(savedReview)
    }

    @Transactional
    override fun getReviewById(reviewId: Long): ReviewResponse {
        val review =
            reviewRepository.findByIdAndDeletedAtIsNull(reviewId)
                ?: throw ModelNotFoundException("Review", reviewId)

        return ReviewResponse.toReviewResponse(review)
    }

    @Transactional
    override fun getPaginatedReviewList(
        productId: Long,
        pageable: Pageable,
    ): Page<ReviewResponse> {
        val reviewPage = reviewRepository.findAllByProductIdAndDeletedAtIsNull(productId, pageable)
        return reviewPage.map { review -> ReviewResponse.toReviewResponse(review) }
    }

    @Transactional
    override fun updateReview(
        reviewId: Long,
        request: UpdateReviewRequest,
    ): ReviewResponse {
        val review =
            reviewRepository.findByIdAndDeletedAtIsNull(reviewId)
                ?: throw ModelNotFoundException("Review", reviewId)

        validateRating(request.rating)

        request.toUpdateReview(review)

        val updatedReview = reviewRepository.save(review)

        return ReviewResponse.toReviewResponse(updatedReview)
    }

    @Transactional
    override fun deleteReview(reviewId: Long) {
        val review =
            reviewRepository.findByIdOrNull(reviewId)
                ?: throw ModelNotFoundException("Review", reviewId)

        review.deletedAt = LocalDateTime.now()
        reviewRepository.save(review)
    }
}
