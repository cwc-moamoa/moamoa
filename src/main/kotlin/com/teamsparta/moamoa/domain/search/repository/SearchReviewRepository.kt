package com.teamsparta.moamoa.domain.search.repository

import com.teamsparta.moamoa.domain.review.model.Review
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface SearchReviewRepository : JpaRepository<Review, Long> {
    fun findReviewsByTitleContaining(
        keyword: String,
        pageable: Pageable,
    ): Page<Review>

    fun findAllByDeletedAtIsNullOrderByLikesDesc(pageable: Pageable): List<Review> // 좋아요순 검색을 위해 추가함
}
