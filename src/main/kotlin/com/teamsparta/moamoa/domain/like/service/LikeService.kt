package com.teamsparta.moamoa.domain.like.service

interface LikeService {
    fun addLikeToProduct(
        productId: Long,
        socialUserId: Long,
    )

    fun removeLikeFromProduct(
        productId: Long,
        socialUserId: Long,
    )

    fun addLikeToReview(
        reviewId: Long,
        socialUserId: Long,
    )

    fun removeLikeFromReview(
        reviewId: Long,
        socialUserId: Long,
    )
}