package com.teamsparta.moamoa.domain.like.controller

import com.teamsparta.moamoa.domain.like.service.LikeService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/likes")
class LikeController(private val likeService: LikeService) {
    @Operation(summary = "상품에 '좋아요' 추가", description = "특정 상품에 대한 사용자의 '좋아요'를 추가합니다.")
    @PostMapping("/product/{productId}/socialUser/{socialUserId}")
    fun addLikeToProduct(
        @Parameter(description = "상품 ID")@PathVariable productId: Long,
        @Parameter(description = "소셜 사용자 ID")@PathVariable socialUserId: Long,
    ) {
        likeService.addLikeToProduct(productId, socialUserId)
    }

    @Operation(summary = "상품에서 '좋아요' 제거", description = "특정 상품에 대한 사용자의 '좋아요'를 제거합니다.")
    @DeleteMapping("/product/{productId}/socialUser/{socialUserId}")
    fun removeLikeFromProduct(
        @Parameter(description = "상품 ID")@PathVariable productId: Long,
        @Parameter(description = "소셜 사용자 ID")@PathVariable socialUserId: Long,
    ): ResponseEntity<String> {
        likeService.removeLikeFromProduct(productId, socialUserId)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    @Operation(summary = "리뷰에 '좋아요' 추가", description = "특정 리뷰에 대한 사용자의 '좋아요'를 추가합니다.")
    @PostMapping("/review/{reviewId}/socialUser/{socialUserId}")
    fun addLikeToReview(
        @Parameter(description = "리뷰 ID") @PathVariable reviewId: Long,
        @Parameter(description = "소셜 사용자 ID") @PathVariable socialUserId: Long,
    ) {
        likeService.addLikeToReview(reviewId, socialUserId)
    }

    @Operation(summary = "리뷰에서 '좋아요' 제거", description = "특정 리뷰에 대한 사용자의 '좋아요'를 제거합니다.")
    @DeleteMapping("/review/{reviewId}/socialUser/{socialUserId}")
    fun removeLikeFromReview(
        @Parameter(description = "리뷰 ID") @PathVariable reviewId: Long,
        @Parameter(description = "소셜 사용자 ID") @PathVariable socialUserId: Long,
    ): ResponseEntity<String> {
        likeService.removeLikeFromReview(reviewId, socialUserId)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }
}
