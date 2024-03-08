 package com.teamsparta.moamoa.like.service

 import com.teamsparta.moamoa.exception.ModelNotFoundException
 import com.teamsparta.moamoa.like.model.Like
 import com.teamsparta.moamoa.like.repository.LikeRepository
 import com.teamsparta.moamoa.product.repository.ProductRepository
 import com.teamsparta.moamoa.review.repository.ReviewRepository
 import org.springframework.stereotype.Service

 @Service
 class LikeServiceImpl(
//     private val userRepository: UserRepository,
     private val productRepository: ProductRepository,
     private val likeRepository: LikeRepository,
     private val reviewRepository: ReviewRepository
 ) : LikeService {

     override fun addLikeToProduct(productId: Long) {
         val product = productRepository.findById(productId)
             .orElseThrow { throw ModelNotFoundException("Product", productId) }
         if (likeRepository.findByProduct(product) == null) {
             likeRepository.save(Like(product = product, status = true))

             // 상품 좋아요 수 증가
             product.likesCount++
             productRepository.save(product)
         }
     }

     override fun removeLikeFromProduct(productId: Long) {
         val product = productRepository.findById(productId)
             .orElseThrow { throw ModelNotFoundException("Product", productId) }
         likeRepository.findByProduct(product)?.let {
             likeRepository.delete(it)

             // 상품 좋아요 수 감소
             if(product.likesCount > 0) {
                 product.likesCount--
                 productRepository.save(product)
             }
         }
     }

     override fun addLikeToReview(reviewId: Long) {
         val review = reviewRepository.findById(reviewId)
             .orElseThrow { throw ModelNotFoundException("Review", reviewId) }
         if (likeRepository.findByReview(review) == null) {
             likeRepository.save(Like(review = review, status = true))

             val product = review.product
             product.likesCount++
             productRepository.save(product)
         }
     }

     override fun removeLikeFromReview(reviewId: Long) {
         val review = reviewRepository.findById(reviewId)
             .orElseThrow { throw ModelNotFoundException("Review", reviewId) }
         likeRepository.findByReview(review)?.let {
             likeRepository.delete(it)
         }
     }
 }
// override fun addLikeToProduct(productId: Long, userId: Long) {
//     val product = productRepository.findById(productId)
//         .orElseThrow { throw ModelNotFoundException("Product", productId) }
//     val user = userRepository.findById(userId)
//         .orElseThrow{ throw ModelNotFoundException("User", userId) }
//     if (likeRepository.findByProductAndUser(product, user) == null) {
//         likeRepository.save(Like(product = product, user = user, status = true))
//     }
// }
//
// override fun removeLikeFromProduct(productId: Long, userId: Long) {
//     val product = productRepository.findById(productId)
//         .orElseThrow { throw ModelNotFoundException("Product", productId) }
//     val user = userRepository.findById(userId)
//         .orElseThrow{ throw ModelNotFoundException("User", userId) }
//     likeRepository.findByProductAndUser(product, user)?.let {
//         likeRepository.delete(it)
//     }
// }
//
// override fun addLikeToReview(reviewId: Long, userId: Long) {
//     val review = reviewRepository.findById(reviewId)
//         .orElseThrow { throw ModelNotFoundException("Review", reviewId) }
//     val user = userRepository.findById(userId)
//         .orElseThrow{ throw ModelNotFoundException("User", userId) }
//     if (likeRepository.findByReviewAndUser(review, user) == null) {
//         likeRepository.save(Like(review = review, user = user, status = true))
//     }
// }
//
// override fun removeLikeFromReview(reviewId: Long, userId: Long) {
//     val review = reviewRepository.findById(reviewId)
//         .orElseThrow { throw ModelNotFoundException("Review", reviewId) }
//     val user = userRepository.findById(userId)
//         .orElseThrow{ throw ModelNotFoundException("User", userId) }
//     likeRepository.findByReviewAndUser(review, user)?.let {
//         likeRepository.delete(it)
//     }
// }
// }






//     override fun getLikedProducts(userId: Long): List<LikeResponse> {
//         val likes = likeRepository.findAllByUserId(userId)
//         return likes.map { like ->
//             LikeResponse(
//                 userId = like.user.id,
//                 productId = like.product.id,
//                 likesCount = like.product.likesCount
//             )
//         }
//     }
//     override fun getLikedReviews(userId: Long): List<LikeResponse> {
//         val likes = likeRepository.findAllByUserIdAndReviewNotNull(userId)
//         return likes.map { like ->
//             LikeResponse(
//                 userId = like.user.id,
//                 reviewId = like.review.id,
//                 likesCount = like.review.likesCount
//             )
//         }
//     }
