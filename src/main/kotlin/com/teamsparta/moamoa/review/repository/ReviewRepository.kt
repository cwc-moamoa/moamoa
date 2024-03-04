package com.teamsparta.moamoa.review.repository

import com.teamsparta.moamoa.review.model.Review
import org.springframework.data.jpa.repository.JpaRepository

interface ReviewRepository : JpaRepository<Review, Long> {
    fun findByIdAndDeletedAtIsNull(reviewId: Long): Review?
    fun findAllByDeletedAtIsNull(): List<Review>
}