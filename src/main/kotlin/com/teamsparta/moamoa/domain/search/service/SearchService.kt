package com.teamsparta.moamoa.domain.search.service

import com.teamsparta.moamoa.domain.product.model.Product
import com.teamsparta.moamoa.domain.review.model.Review
import com.teamsparta.moamoa.domain.search.dto.ProductSearchResponse
import com.teamsparta.moamoa.domain.search.dto.ReviewSearchResponse
import com.teamsparta.moamoa.domain.search.model.SearchHistory
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Page

 interface SearchService {

    fun searchProductsByLikes(pageable: Pageable): List<ProductSearchResponse>
    fun searchReviewsByLikes(pageable: Pageable): List<ReviewSearchResponse>

     fun searchProducts(keyword: String, pageable: Pageable): Page<Product>
     fun searchReviews(keyword: String, pageable: Pageable): Page<Review>

     fun saveSearchHistory(keyword: String)

     fun getPopularKeywords(): List<SearchHistory>
 }
