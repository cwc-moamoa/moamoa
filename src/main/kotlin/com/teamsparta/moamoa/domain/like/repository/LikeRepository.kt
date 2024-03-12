package com.teamsparta.moamoa.domain.like.repository

import com.teamsparta.moamoa.domain.like.model.Like
import com.teamsparta.moamoa.domain.product.model.Product
import com.teamsparta.moamoa.domain.review.model.Review
import com.teamsparta.moamoa.domain.socialUser.model.SocialUser
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface LikeRepository : JpaRepository<Like, Long> {
    fun findByReviewAndUser(
        review: Review,
        user: SocialUser,
    ): Like?

    fun findByProductAndUser(
        product: Product,
        user: SocialUser,
    ): Like?
}
