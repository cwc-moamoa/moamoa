package com.teamsparta.moamoa.domain.like.service

import com.teamsparta.moamoa.domain.like.model.Like
import com.teamsparta.moamoa.domain.like.repository.LikeRepository
import com.teamsparta.moamoa.domain.product.repository.ProductRepository
import com.teamsparta.moamoa.domain.review.repository.ReviewRepository
import com.teamsparta.moamoa.domain.socialUser.repository.SocialUserRepository
import com.teamsparta.moamoa.exception.ModelNotFoundException
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class LikeServiceImpl(
    private val productRepository: ProductRepository,
    private val likeRepository: LikeRepository,
    private val reviewRepository: ReviewRepository,
    private val socialUserRepository: SocialUserRepository,
) : LikeService {
    @Transactional
    override fun addLikeToProduct(
        productId: Long,
        providerId: Long,
    ) {
        val product =
            productRepository.findById(productId)
                .orElseThrow { throw ModelNotFoundException("Product", productId) }

        if (product.deletedAt != null) {
            throw Exception("없는 상품입니다")
        }

        val user =
            socialUserRepository.findByProviderId(providerId.toString())
                .orElseThrow { throw ModelNotFoundException("User", providerId) }

        val existingLike = likeRepository.findByProductAndSocialUser(product, user)
        if (existingLike != null) {
            throw IllegalArgumentException("이미 좋아요를 누른 상품입니다")
        }

        likeRepository.save(Like(product = product, socialUser = user, status = true))
        product.likes++
        productRepository.save(product)
    }

    @Transactional
    override fun removeLikeFromProduct(
        productId: Long,
        providerId: Long,
    ) {
        val product =
            productRepository.findById(productId)
                .orElseThrow { throw ModelNotFoundException("Product", productId) }
        val user =
            socialUserRepository.findByProviderId(providerId.toString())
                .orElseThrow { throw ModelNotFoundException("User", providerId) }

        val like = likeRepository.findByProductAndSocialUser(product, user)
        if (like != null) {
            likeRepository.delete(like)

            if (product.likes > 0) {
                product.likes--
                productRepository.save(product)
            }
        } else {
            throw IllegalArgumentException("좋아요가 없습니다")
        }
    }

    @Transactional
    override fun addLikeToReview(
        reviewId: Long,
        providerId: Long,
    ) {
        val review =
            reviewRepository.findById(reviewId)
                .orElseThrow { throw ModelNotFoundException("Review", reviewId) }

        if (review.deletedAt != null) {
            throw Exception("없는 리뷰입니다")
        }

        val user =
            socialUserRepository.findByProviderId(providerId.toString())
                .orElseThrow { throw ModelNotFoundException("User", providerId) }

        val existingLike = likeRepository.findByReviewAndSocialUser(review, user)
        if (existingLike != null) {
            throw IllegalArgumentException("이미 좋아요를 누른 리뷰입니다")
        }

        likeRepository.save(Like(review = review, socialUser = user, status = true))
        review.likes++
        reviewRepository.save(review)
    }

    @Transactional
    override fun removeLikeFromReview(
        reviewId: Long,
        providerId: Long,
    ) {
        val review =
            reviewRepository.findById(reviewId)
                .orElseThrow { throw ModelNotFoundException("Review", reviewId) }
        val user =
            socialUserRepository.findByProviderId(providerId.toString())
                .orElseThrow { throw ModelNotFoundException("User", providerId) }

        val like = likeRepository.findByReviewAndSocialUser(review, user)
        if (like != null) {
            likeRepository.delete(like)


            if (review.likes > 0) {
                review.likes--
                reviewRepository.save(review)
            }
        } else {
            throw IllegalArgumentException("좋아요가 없습니다")
        }
    }
}
