package com.teamsparta.moamoa.domain.review.controller

import com.teamsparta.moamoa.domain.review.dto.CreateReviewRequest
import com.teamsparta.moamoa.domain.review.dto.ReviewResponse
import com.teamsparta.moamoa.domain.review.dto.UpdateReviewRequest
import com.teamsparta.moamoa.domain.review.service.ReviewService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/products/{productId}/reviews")
class ReviewController(
    private val reviewService: ReviewService
) {
    @PostMapping //(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun createReview(
        @PathVariable productId: Long,
        @RequestBody createReviewRequest: CreateReviewRequest,
//        @RequestPart("image") multipartFile: MultipartFile
    ): ResponseEntity<ReviewResponse> {
        val result = reviewService.createReview(productId, createReviewRequest)
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(result)
    }

    @GetMapping("/{reviewId}")
    fun getReviewById(
        @PathVariable reviewId: Long,
    ): ResponseEntity<ReviewResponse> {
        val review = reviewService.getReviewById(reviewId)
        return ResponseEntity.ok(review)
    }

    @GetMapping
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
        @RequestBody updateReviewRequest: UpdateReviewRequest,
    ): ResponseEntity<ReviewResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(reviewService.updateReview(reviewId, updateReviewRequest))
    }

    @DeleteMapping("/{reviewId}")
    fun deleteReview(
        @PathVariable reviewId: Long,
    ): ResponseEntity<String> {
        reviewService.deleteReview(reviewId)
        val deleteReviewSuccessMessage = "댓글이 성공적으로 삭제되었습니다!"
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(deleteReviewSuccessMessage)
    }
}