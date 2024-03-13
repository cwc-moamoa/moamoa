package com.teamsparta.moamoa.domain.search.repository


import com.teamsparta.moamoa.domain.search.dto.ProductSearchResponse
import com.teamsparta.moamoa.domain.search.dto.ReviewSearchResponse
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Page


interface CustomSearchRepository {
    fun searchProductsByLikes(pageable: Pageable): Page<ProductSearchResponse>
    fun searchReviewsByLikes(pageable: Pageable): Page<ReviewSearchResponse>
}
