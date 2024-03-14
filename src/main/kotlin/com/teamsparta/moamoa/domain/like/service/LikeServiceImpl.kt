package com.teamsparta.moamoa.domain.like.service

// import com.teamsparta.moamoa.domain.like.model.Like
// import com.teamsparta.moamoa.domain.like.repository.LikeRepository
// import com.teamsparta.moamoa.domain.product.repository.ProductRepository
// import com.teamsparta.moamoa.domain.review.repository.ReviewRepository
// import com.teamsparta.moamoa.domain.user.repository.UserRepository
// import com.teamsparta.moamoa.exception.ModelNotFoundException
// import jakarta.transaction.Transactional
// import org.springframework.stereotype.Service
//
// @Service
// class LikeServiceImpl(
//    private val productRepository: ProductRepository,
//    private val likeRepository: LikeRepository,
//    private val reviewRepository: ReviewRepository,
//    private val userRepository: UserRepository,
// ) : LikeService {
//    @Transactional
//    override fun addLikeToProduct(
//        productId: Long,
//        userId: Long,
//    ) {
//        val product =
//            productRepository.findById(productId)
//                .orElseThrow { throw ModelNotFoundException("Product", productId) }
//
//        if (product.deletedAt != null) {
//            throw Exception("없는 상품입니다")
//        }
//
//        val user =
//            userRepository.findById(userId)
//                .orElseThrow { throw ModelNotFoundException("User", userId) }
//
//        if (likeRepository.findByProductAndUser(product, user) == null) { // 여기가 해당 상품에 유저가 널이여야, 좋아요를 안해야한다는거
//            likeRepository.save(Like(product = product, user = user, status = true))
//
//            product.likes++
//            productRepository.save(product)
//        }
//    }
//
//    @Transactional
//    override fun removeLikeFromProduct(
//        productId: Long,
//        userId: Long,
//    ) {
//        val product =
//            productRepository.findById(productId)
//                .orElseThrow { throw ModelNotFoundException("Product", productId) }
//        val user =
//            userRepository.findById(userId)
//                .orElseThrow { throw ModelNotFoundException("User", userId) }
//
//        likeRepository.findByProductAndUser(product, user)?.let {
//            likeRepository.delete(it) // findByProductAndUser로 Like가 null이 아니라면 해당 "좋아요" 기록을 삭제
//
//            if (product.likes > 0) {
//                product.likes--
//                productRepository.save(product)
//            }
//        }
//    }
//
//    @Transactional
//    override fun addLikeToReview(
//        reviewId: Long,
//        userId: Long,
//    ) {
//        val review =
//            reviewRepository.findById(reviewId)
//                .orElseThrow { throw ModelNotFoundException("Review", reviewId) }
//
//        if (review.deletedAt != null) {
//            throw Exception("없는 리뷰입니다")
//        }
//
//        val user =
//            userRepository.findById(userId)
//                .orElseThrow { throw ModelNotFoundException("User", userId) }
//
//        if (likeRepository.findByReviewAndUser(review, user) == null) {
//            likeRepository.save(Like(review = review, user = user, status = true))
//            review.likes++
//            reviewRepository.save(review)
//        }
//    }
//
//    @Transactional
//    override fun removeLikeFromReview(
//        reviewId: Long,
//        userId: Long,
//    ) {
//        val review =
//            reviewRepository.findById(reviewId)
//                .orElseThrow { throw ModelNotFoundException("Review", reviewId) }
//        val user =
//            userRepository.findById(userId)
//                .orElseThrow { throw ModelNotFoundException("User", userId) }
//
//        likeRepository.findByReviewAndUser(review, user)?.let {
//            likeRepository.delete(it)
//
//            if (review.likes > 0) {
//                review.likes--
//                reviewRepository.save(review)
//            }
//        }
//    }
// }
