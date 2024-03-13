package com.teamsparta.moamoa.domain.search.repository

import com.teamsparta.moamoa.domain.product.model.Product
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Page
import org.springframework.data.jpa.repository.JpaRepository

interface SearchProductRepository : JpaRepository<Product, Long> {
    fun findByTitleContaining(keyword: String, pageable: Pageable): Page<Product>//제목에 입력한 키워드를 찿는 기능
    fun findAllByDeletedAtIsNullOrderByLikesDesc(pageable: Pageable): List<Product> // 좋아요순 검색을 위해 추가함

}