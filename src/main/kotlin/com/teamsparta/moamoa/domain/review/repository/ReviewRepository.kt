package com.teamsparta.moamoa.domain.review.repository

import com.teamsparta.moamoa.domain.review.model.Review
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface ReviewRepository : JpaRepository<Review, Long> {
    // Id를 조회할 때 DeletedAt이 null 인 경우 삭제된 리뷰는 조회되지않음
    fun findByIdAndDeletedAtIsNull(reviewId: Long): Review?

    // r.deletedAt IS NULL 조건을 통해 리뷰가 삭제되지 않았음을 확인하고, 상품이 삭제되지 않았을 경우에만 리뷰 조회함
    @Query("SELECT r FROM Review r WHERE r.product.id = :productId AND r.deletedAt IS NULL AND r.product.deletedAt IS NULL")
    fun findAllByProductIdAndDeletedAtIsNull(
        productId: Long,
        pageable: Pageable,
    ): Page<Review>

    fun findByProductIdAndDeletedAtIsNull(productId: Long): List<Review>

    fun findByOrderId(orderId: Long): Optional<Review>

}
