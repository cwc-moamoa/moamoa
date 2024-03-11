package com.teamsparta.moamoa.domain.like.service

import com.teamsparta.moamoa.domain.like.model.Like
import com.teamsparta.moamoa.domain.like.repository.LikeRepository
import com.teamsparta.moamoa.domain.product.repository.ProductRepository
import com.teamsparta.moamoa.domain.review.repository.ReviewRepository
import com.teamsparta.moamoa.domain.user.repository.UserRepository
import com.teamsparta.moamoa.exception.ModelNotFoundException
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class LikeServiceImpl(
    private val productRepository: ProductRepository,
    private val likeRepository: LikeRepository,
    private val reviewRepository: ReviewRepository,
    private val userRepository: UserRepository,
) : LikeService {
    @Transactional
    override fun addLikeToProduct(
        productId: Long,
        userId: Long,
    ) {
        val product =
            productRepository.findById(productId)
                .orElseThrow { throw ModelNotFoundException("Product", productId) }

        if (product.deletedAt != null) {
            throw Exception("없는 상품입니다")
        } // 이게 소프트 딜리트 감지임

        val user =
            userRepository.findById(userId)
                .orElseThrow { throw ModelNotFoundException("User", userId) }

        if (likeRepository.findByProductAndUser(product, user) == null) {
            likeRepository.save(Like(product = product, user = user, status = true)) // 여기가 널이여야만 생성하는거

            // 이게 증가 로직임
            product.likes++
            productRepository.save(product)
        }
    }

    @Transactional
    override fun removeLikeFromProduct(
        productId: Long,
        userId: Long,
    ) {
        val product =
            productRepository.findById(productId)
                .orElseThrow { throw ModelNotFoundException("Product", productId) }
        val user =
            userRepository.findById(userId)
                .orElseThrow { throw ModelNotFoundException("User", userId) }

        likeRepository.findByProductAndUser(product, user)?.let {
            likeRepository.delete(it)

            // 아래가 감소의 로직임 기록해두자
            if (product.likes > 0) {
                product.likes--
                productRepository.save(product)
            }
        }
    }

    @Transactional
    override fun addLikeToReview(
        reviewId: Long,
        userId: Long,
    ) {
        val review =
            reviewRepository.findById(reviewId)
                .orElseThrow { throw ModelNotFoundException("Review", reviewId) }

        if (review.deletedAt != null) {
            throw Exception("없는 리뷰입니다")
        }

        val user =
            userRepository.findById(userId)
                .orElseThrow { throw ModelNotFoundException("User", userId) }

        if (likeRepository.findByReviewAndUser(review, user) == null) {
            likeRepository.save(Like(review = review, user = user, status = true))
            review.likes++
            reviewRepository.save(review)
        }
    }

    @Transactional
    override fun removeLikeFromReview(
        reviewId: Long,
        userId: Long,
    ) {
        val review =
            reviewRepository.findById(reviewId)
                .orElseThrow { throw ModelNotFoundException("Review", reviewId) }
        val user =
            userRepository.findById(userId)
                .orElseThrow { throw ModelNotFoundException("User", userId) }

        likeRepository.findByReviewAndUser(review, user)?.let {
            likeRepository.delete(it)

            if (review.likes > 0) {
                review.likes--
                reviewRepository.save(review)
            }
        }
    }
}
