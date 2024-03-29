package com.teamsparta.moamoa.domain.review.controller

import com.teamsparta.moamoa.domain.review.dto.CreateReviewRequest
import com.teamsparta.moamoa.domain.review.dto.ReviewResponse
import com.teamsparta.moamoa.domain.review.dto.ReviewResponseByList
import com.teamsparta.moamoa.domain.review.dto.UpdateReviewRequest
import com.teamsparta.moamoa.domain.review.service.ReviewService
import com.teamsparta.moamoa.infra.security.UserPrincipal
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Validated
@RestController
@CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
class ReviewController(
    private val reviewService: ReviewService,
) {
    @PostMapping("/reviews/create/{orderId}")
    fun createReview(
        @PathVariable orderId: Long,
        @AuthenticationPrincipal user: UserPrincipal,
        @Valid @RequestBody createReviewRequest: CreateReviewRequest,
    ): ResponseEntity<List<ReviewResponse>> {
        val result = reviewService.createReview(user.id, createReviewRequest, orderId)
        val results = listOf(result)
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(results)
    }

    @GetMapping("/review/{reviewId}")
    fun getReviewById(
        @PathVariable reviewId: Long,
    ): ResponseEntity<ReviewResponse> {
        val review = reviewService.getReviewById(reviewId)
        return ResponseEntity.ok(review)
    }

    @GetMapping("/product/{productId}")
    fun getPaginatedReviewList(
        @PathVariable productId: Long,
        @PageableDefault(size = 15, sort = ["id"], direction = Sort.Direction.ASC) pageable: Pageable,
    ): ResponseEntity<Page<ReviewResponse>> {
        val reviews = reviewService.getPaginatedReviewList(productId, pageable)
        return ResponseEntity.ok(reviews)
    }

    @PutMapping("/{reviewId}")
    fun updateReview(
        @PathVariable reviewId: Long,
        @AuthenticationPrincipal user: UserPrincipal,
        @Valid @RequestBody updateReviewRequest: UpdateReviewRequest,
    ): ResponseEntity<ReviewResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(reviewService.updateReview(reviewId, user.id, updateReviewRequest))
    }

    @DeleteMapping("/{reviewId}")
    fun deleteReview(
        @PathVariable reviewId: Long,
        @AuthenticationPrincipal user: UserPrincipal,
    ): ResponseEntity<String> {
        reviewService.deleteReview(reviewId, user.id)
        val deleteReviewSuccessMessage = "댓글이 성공적으로 삭제되었습니다!"
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(deleteReviewSuccessMessage)
    }

    // 프론트 연결용 컨트롤러
    @GetMapping("/products/{productId}/reviews")
    fun getProductReviews(
        @PathVariable productId: Long,
    ): ResponseEntity<List<ReviewResponseByList>> {
        val reviews = reviewService.getReviewsByProductId(productId)
        return ResponseEntity.ok(reviews)
    }

    @GetMapping("/review/get/{orderId}")
    fun getReviewByOrderId(@PathVariable orderId: Long): ResponseEntity<ReviewResponse> {
        val review = reviewService.getReviewByOrderId(orderId)
        return ResponseEntity.ok(review)
    }
}
