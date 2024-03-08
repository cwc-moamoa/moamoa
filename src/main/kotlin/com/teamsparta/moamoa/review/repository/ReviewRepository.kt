package com.teamsparta.moamoa.review.repository

import com.teamsparta.moamoa.review.model.Review
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface ReviewRepository : JpaRepository<Review, Long> {
    fun findByIdAndDeletedAtIsNull(reviewId: Long): Review?

    fun findAllByProductIdAndDeletedAtIsNull(productId: Long, pageable: Pageable): Page<Review>
}
