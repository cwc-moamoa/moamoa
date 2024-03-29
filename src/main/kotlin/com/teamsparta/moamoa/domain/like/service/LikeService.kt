package com.teamsparta.moamoa.domain.like.service

interface LikeService {
    fun addLikeToProduct(
        productId: Long,
        providerId: Long,
    )

    fun removeLikeFromProduct(
        productId: Long,
        providerId: Long,
    )

    fun addLikeToReview(
        reviewId: Long,
        providerId: Long,
    )

    fun removeLikeFromReview(
        reviewId: Long,
        providerId: Long,
    )
}
