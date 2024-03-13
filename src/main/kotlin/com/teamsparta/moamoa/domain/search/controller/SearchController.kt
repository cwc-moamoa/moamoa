package com.teamsparta.moamoa.domain.search.controller

import com.teamsparta.moamoa.domain.product.model.Product
import com.teamsparta.moamoa.domain.review.model.Review
import com.teamsparta.moamoa.domain.search.dto.ProductSearchResponse
import com.teamsparta.moamoa.domain.search.dto.ReviewSearchResponse
import com.teamsparta.moamoa.domain.search.model.SearchHistory
import com.teamsparta.moamoa.domain.search.service.SearchService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/search")
class SearchController(
    private val searchService: SearchService,
) {
    @GetMapping("/products/likes")
    fun searchProductsByLikes(
        @PageableDefault(size = 15, sort = ["likes"], direction = Sort.Direction.DESC) pageable: Pageable,
    ): ResponseEntity<List<ProductSearchResponse>> {
        val products = searchService.searchProductsByLikes(pageable)
        return ResponseEntity.status(HttpStatus.OK).body(products)
    }

    @GetMapping("/reviews/likes")
    fun searchReviewsByLikes(
        @PageableDefault(size = 15, sort = ["likes"], direction = Sort.Direction.DESC) pageable: Pageable,
    ): ResponseEntity<List<ReviewSearchResponse>> {
        val reviews = searchService.searchReviewsByLikes(pageable)
        return ResponseEntity.status(HttpStatus.OK).body(reviews)
    }

    @GetMapping("/products")
    fun searchProducts(
        @RequestParam keyword: String,
        @PageableDefault(size = 15) pageable: Pageable, // sort뒤를 없애면 그부분 없어질줄 알았는대 안없어짐
    ): ResponseEntity<Page<Product>> {
        val products = searchService.searchProducts(keyword, pageable)
        return ResponseEntity.status(HttpStatus.OK).body(products)
    }

    @GetMapping("/reviews")
    fun searchReviews(
        @RequestParam keyword: String,
        @PageableDefault(size = 15) pageable: Pageable,
    ): ResponseEntity<Page<Review>> {
        val reviews = searchService.searchReviews(keyword, pageable)
        return ResponseEntity.status(HttpStatus.OK).body(reviews)
    }

    @GetMapping("/popular")
    fun getPopularKeywords(): ResponseEntity<List<SearchHistory>> {
        val popularKeywords = searchService.getPopularKeywords()
        return ResponseEntity.status(HttpStatus.OK).body(popularKeywords)
    }

}
