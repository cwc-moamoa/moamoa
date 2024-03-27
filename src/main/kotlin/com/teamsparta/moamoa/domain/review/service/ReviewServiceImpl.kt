package com.teamsparta.moamoa.domain.review.service

import com.teamsparta.moamoa.domain.order.repository.OrderRepository
import com.teamsparta.moamoa.domain.product.repository.ProductRepository
import com.teamsparta.moamoa.domain.review.dto.CreateReviewRequest
import com.teamsparta.moamoa.domain.review.dto.ReviewResponse
import com.teamsparta.moamoa.domain.review.dto.ReviewResponseByList
import com.teamsparta.moamoa.domain.review.dto.UpdateReviewRequest
import com.teamsparta.moamoa.domain.review.repository.ReviewRepository
import com.teamsparta.moamoa.domain.socialUser.repository.SocialUserRepository
import com.teamsparta.moamoa.exception.ModelNotFoundException
import com.teamsparta.moamoa.infra.security.UserPrincipal
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
    private val socialUserRepository: SocialUserRepository,
    private val orderRepository: OrderRepository,
) : ReviewService {


    @Transactional
    override fun createReview(
        productId: Long,
        socialUser: UserPrincipal,
        createReviewRequest: CreateReviewRequest,
        orderId: Long,
    ): ReviewResponse {
        // 주문에 대한 리뷰가 이미 있는지 확인
        reviewRepository.findByOrderId(orderId).ifPresent {
            throw IllegalStateException("이미 이 주문에 대한 리뷰가 작성되었습니다.")
        }
//이메일로 유저가 있는지 확인
        val socialUser = socialUserRepository.findByEmail(socialUser.email)
            .orElseThrow { ModelNotFoundException("User not found", socialUser.id) }
//제품이 있는지, 소프트딜리트인지 확인
        val product = productRepository.findByIdAndDeletedAtIsNull(productId)
            .orElseThrow { ModelNotFoundException("Product not found or deleted", productId) }
//아이디가 없으면
        val userId = socialUser.id ?: throw IllegalArgumentException("사용자 ID가 없습니다")
// orderId와 userId로 주문을 했는지
        val order = orderRepository.findByIdAndSocialUserId(orderId, userId)
            .orElseThrow { ModelNotFoundException("주문내역을 확인할 수 없습니다", orderId) }
        val review = createReviewRequest.toReview(product, socialUser, order)

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

    override fun getReviewsByProductId(productId: Long): List<ReviewResponseByList> {
        val reviews = reviewRepository.findByProductIdAndDeletedAtIsNull(productId)
        return ReviewResponseByList.toReviewResponseByList(reviews)
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
        socialUser: UserPrincipal,
        request: UpdateReviewRequest,
    ): ReviewResponse {
        val review =
            reviewRepository.findByIdAndDeletedAtIsNull(reviewId)
                ?: throw ModelNotFoundException("Review", reviewId)

        if (review.socialUser.email != socialUser.email) {
            throw IllegalAccessException("권한이 없습니다.")
        }


        request.toUpdateReview(review)

        val updatedReview = reviewRepository.save(review)

        return ReviewResponse.toReviewResponse(updatedReview)
    }

    @Transactional
    override fun deleteReview(
        reviewId: Long,
        socialUser: UserPrincipal,
    ) {
        val review =
            reviewRepository.findByIdOrNull(reviewId)
                ?: throw ModelNotFoundException("Review", reviewId)

        if (review.socialUser.email != socialUser.email) {
            throw IllegalAccessException("권한이 없습니다.")
        }

        if (review.deletedAt != null) {
            throw Exception("이미 삭제된 리뷰입니다.")
        }

        review.deletedAt = LocalDateTime.now()
        reviewRepository.save(review)
    }
}
