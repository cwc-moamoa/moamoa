package com.teamsparta.moamoa.domain.like.service

interface LikeService {
    fun addLikeToProduct(
        socialUserId: Long,
        productId: Long,
    )

    fun removeLikeFromProduct(
        socialUserId: Long,
        productId: Long,
    )

    fun addLikeToReview(
        socialUserId: Long,
        reviewId: Long,
    )

    fun removeLikeFromReview(
        socialUserId: Long,
        reviewId: Long,
    )
}
