package com.teamsparta.moamoa.domain.search.service

import com.teamsparta.moamoa.domain.search.dto.ProductSearchResponse
import com.teamsparta.moamoa.domain.search.dto.ReviewSearchResponse
import org.springframework.data.domain.Pageable

 interface SearchService {

    fun searchProductsByLikes(pageable: Pageable): List<ProductSearchResponse>
    fun searchReviewsByLikes(pageable: Pageable): List<ReviewSearchResponse>
 }
