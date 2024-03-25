package com.teamsparta.moamoa.domain.search.service

import com.teamsparta.moamoa.domain.search.dto.ProductSearchResponse
import com.teamsparta.moamoa.domain.search.dto.ReviewSearchResponse
import com.teamsparta.moamoa.domain.search.model.SearchHistory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface SearchService {
    fun searchProductsByLikes(pageable: Pageable): List<ProductSearchResponse>

    fun searchReviewsByLikes(pageable: Pageable): List<ReviewSearchResponse>

    fun searchProducts(
        keyword: String,
        pageable: Pageable,
    ): Page<ProductSearchResponse>

    fun searchReviews(
        keyword: String,
        pageable: Pageable,
    ): Page<ReviewSearchResponse>

    fun saveSearchHistory(keyword: String)

    fun getPopularKeywords(): List<SearchHistory>

    fun searchProductsByRating(pageable: Pageable): List<ProductSearchResponse> // 별점 평균이 높은 순으로 검색을 위해 추가함
}
