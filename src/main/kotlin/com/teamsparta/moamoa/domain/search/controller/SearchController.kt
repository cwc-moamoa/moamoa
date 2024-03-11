package com.teamsparta.moamoa.domain.search.controller

import com.teamsparta.moamoa.domain.search.dto.ProductSearchResponse
import com.teamsparta.moamoa.domain.search.dto.ReviewSearchResponse
import com.teamsparta.moamoa.domain.search.service.SearchService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/search")
class SearchController(
    private val searchService: SearchService,
) {
    @GetMapping("/products/likes")
    fun searchProductsByLikes(): ResponseEntity<List<ProductSearchResponse>> {
        val productResults = searchService.searchProductsByLikes()
        return ResponseEntity.ok(productResults)
    }

    @GetMapping("/reviews/likes")
    fun searchReviewsByLikes(): ResponseEntity<List<ReviewSearchResponse>> {
        val reviewResults = searchService.searchReviewsByLikes()
        return ResponseEntity.ok(reviewResults)
    }
}
