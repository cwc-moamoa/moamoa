package com.teamsparta.moamoa.domain.like.service

import com.teamsparta.moamoa.domain.like.model.Like
import com.teamsparta.moamoa.domain.like.repository.LikeRepository
import com.teamsparta.moamoa.domain.product.repository.ProductRepository
import com.teamsparta.moamoa.domain.review.repository.ReviewRepository
import com.teamsparta.moamoa.domain.socialUser.repository.SocialUserRepository
import com.teamsparta.moamoa.exception.DuplicateParticipationException
import com.teamsparta.moamoa.exception.ModelNotFoundException
import jakarta.transaction.Transactional
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class LikeServiceImpl(
    private val productRepository: ProductRepository,
    private val likeRepository: LikeRepository,
    private val reviewRepository: ReviewRepository,
    private val socialUserRepository: SocialUserRepository,
) : LikeService {

    private val logger: Logger = LoggerFactory.getLogger(LikeServiceImpl::class.java)
    @Transactional
    override fun addLikeToProduct(
        productId: Long,
        socialUserId: Long,
    ) {
            val product = productRepository.findByIdAndDeletedAtIsNull(productId).orElseThrow { throw ModelNotFoundException("Product", productId) }

            val user = socialUserRepository.findById(socialUserId).orElseThrow { throw ModelNotFoundException("User", socialUserId) }

            val existingLike = likeRepository.findByProductAndSocialUser(product, user)
            if (existingLike != null) {
                throw DuplicateParticipationException("중복된 시도입니다.")
            }

            likeRepository.save(Like(product = product, socialUser = user, status = true))
            product.likes++
            productRepository.save(product)
        }



    @Transactional
    override fun removeLikeFromProduct(
        productId: Long,
        socialUserId: Long,
    ) {
            val product =
                productRepository.findById(productId)
                    .orElseThrow { throw ModelNotFoundException("Product", productId) }
            val user =
                socialUserRepository.findById(socialUserId)
                    .orElseThrow { throw ModelNotFoundException("User", socialUserId) }

            val like = likeRepository.findByProductAndSocialUser(product, user)
            if (like != null) {
                if (like.socialUser.id != socialUserId) {
                    throw ModelNotFoundException("Like", null )
                }
                likeRepository.delete(like)

                if (product.likes > 0) {
                    product.likes--
                    productRepository.save(product)
                }
            } else {
                throw ModelNotFoundException("Like", "")
            }
    }

    @Transactional
    override fun addLikeToReview(
        reviewId: Long,
        socialUserId: Long,
    ) {
            val review =
                reviewRepository.findById(reviewId)
                    .orElseThrow { throw ModelNotFoundException("Review", reviewId) }

            if (review.deletedAt != null) {
                throw ModelNotFoundException("Review", reviewId)
            }

            val user =
                socialUserRepository.findById(socialUserId)
                    .orElseThrow { throw ModelNotFoundException("User", socialUserId) }

            val existingLike = likeRepository.findByReviewAndSocialUser(review, user)
            if (existingLike != null) {
                throw DuplicateParticipationException("이미 좋아요를 누른 리뷰입니다")
            }

            likeRepository.save(Like(review = review, socialUser = user, status = true))
            review.likes++
            reviewRepository.save(review)
    }

    @Transactional
    override fun removeLikeFromReview(
        reviewId: Long,
        socialUserId: Long,
    ) {
            val review =
                reviewRepository.findById(reviewId)
                    .orElseThrow { throw ModelNotFoundException("Review", reviewId) }
            val user =
                socialUserRepository.findById(socialUserId)
                    .orElseThrow { throw ModelNotFoundException("User", socialUserId) }

            val like = likeRepository.findByReviewAndSocialUser(review, user)
            if (like != null) {
                if (like.socialUser.id != socialUserId) throw ModelNotFoundException("Like", like.id)

                likeRepository.delete(like)

                if (review.likes > 0) {
                    review.likes--
                    reviewRepository.save(review)
                }
            } else {
                throw ModelNotFoundException("Like", null)
            }
    }
}
