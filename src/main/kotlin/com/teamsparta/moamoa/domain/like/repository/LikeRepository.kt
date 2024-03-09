package com.teamsparta.moamoa.domain.like.repository

import com.teamsparta.moamoa.domain.like.model.Like
import com.teamsparta.moamoa.domain.product.model.Product
import com.teamsparta.moamoa.domain.review.model.Review
import com.teamsparta.moamoa.domain.user.model.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface LikeRepository : JpaRepository<Like, Long> {
    fun findByReviewAndUser(
        review: Review,
        user: User,
    ): Like?

    fun findByProductAndUser(
        product: Product,
        user: User,
    ): Like?
}
