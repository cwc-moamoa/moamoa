 package com.teamsparta.moamoa.like.service

 interface LikeService {
     fun addLikeToProduct(productId: Long)
     fun removeLikeFromProduct(productId: Long)
     fun addLikeToReview(reviewId: Long)
     fun removeLikeFromReview(reviewId: Long)
}//     fun addLikeToProduct(userId: Long, productId: Long)
// fun removeLikeFromProduct(userId: Long, productId: Long)
// fun addLikeToReview(userId: Long, reviewId: Long)
// fun removeLikeFromReview(userId: Long, reviewId: Long)