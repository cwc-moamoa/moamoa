package com.teamsparta.moamoa.domain.search.repository

import com.teamsparta.moamoa.domain.product.model.Product
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface SearchProductRepository : JpaRepository<Product, Long> {
    fun findByTitleContainingAndDeletedAtIsNull(keyword: String, pageable: Pageable): Page<Product> // 키워드를 찿기

    fun findAllByDeletedAtIsNullOrderByLikesDesc(pageable: Pageable): List<Product> // 좋아요순 검색

    fun findAllByDeletedAtIsNullOrderByRatingAverageDesc(pageable: Pageable): List<Product> // 별점 검색
}
