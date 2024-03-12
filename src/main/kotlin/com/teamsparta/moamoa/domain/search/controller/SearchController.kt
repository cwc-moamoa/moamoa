package com.teamsparta.moamoa.domain.search.controller

import com.teamsparta.moamoa.domain.search.dto.ProductSearchResponse
import com.teamsparta.moamoa.domain.search.dto.ReviewSearchResponse
import com.teamsparta.moamoa.domain.search.service.SearchService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus

@RestController
@RequestMapping("/api/search")
class SearchController(
    private val searchService: SearchService,
) {
    @GetMapping("/products")
    fun searchProductsByLikes(
        @PageableDefault(size = 15, sort = ["likes"], direction = Sort.Direction.DESC) pageable: Pageable
    ): ResponseEntity<List<ProductSearchResponse>> {
        val products = searchService.searchProductsByLikes(pageable)
        return ResponseEntity.status(HttpStatus.OK).body(products)
    }

    @GetMapping("/reviews")
    fun searchReviewsByLikes(
        @PageableDefault(size = 15, sort = ["likes"], direction = Sort.Direction.DESC) pageable: Pageable
    ): ResponseEntity<List<ReviewSearchResponse>> {
        val reviews = searchService.searchReviewsByLikes(pageable)
        return ResponseEntity.status(HttpStatus.OK).body(reviews)
    }
}
//    @GetMapping("/products/likes")
//    fun searchProductsByLikes(): ResponseEntity<List<ProductSearchResponse>> {
//        val productResults = searchService.searchProductsByLikes()
//        return ResponseEntity.ok(productResults)
//    }
//
//    @GetMapping("/reviews/likes")
//    fun searchReviewsByLikes(): ResponseEntity<List<ReviewSearchResponse>> {
//        val reviewResults = searchService.searchReviewsByLikes()
//        return ResponseEntity.ok(reviewResults)

