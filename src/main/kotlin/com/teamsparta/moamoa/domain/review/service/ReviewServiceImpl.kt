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
    @Transactional
    override fun createReview(
        productId: Long,
        createReviewRequest: CreateReviewRequest,
    ): ReviewResponse {
        val product =
            productRepository.findByIdOrNull(productId)
                ?: throw ModelNotFoundException("Product", productId)

        val review = createReviewRequest.toReview(product)

        val savedReview = reviewRepository.save(review)
        return ReviewResponse.toReviewResponse(savedReview)
    }

    @Transactional
    override fun getReviewById(reviewId: Long): ReviewResponse {
        val review =
            reviewRepository.findByIdOrNull(reviewId)
                ?: throw ModelNotFoundException("Review", reviewId)

        return ReviewResponse.toReviewResponse(review)
    }

    @Transactional(readOnly = true)
    override fun getPaginatedReviewList(pageable: Pageable): Page<ReviewResponse> {
        val reviews = reviewRepository.findAll(pageable) // 조건 없이 모든 리뷰 조회
        return reviews.map { review -> ReviewResponse.toReviewResponse(review) }
    }

    @Transactional
    override fun updateReview(
        reviewId: Long,
        request: UpdateReviewRequest,
    ): ReviewResponse {
        val review =
            reviewRepository.findByIdAndDeletedAtIsNull(reviewId)
                ?: throw ModelNotFoundException("Review", reviewId)

//        if (review.deletedAt != null) {
//            throw ReviewDeleteException(reviewId)
//        }

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
