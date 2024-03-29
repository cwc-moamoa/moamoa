package com.teamsparta.moamoa.domain.like.controller

import com.teamsparta.moamoa.domain.like.service.LikeService
import com.teamsparta.moamoa.infra.security.UserPrincipal
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/likes")
class LikeController(private val likeService: LikeService) {
    @Operation(summary = "상품에 '좋아요' 추가", description = "특정 상품에 대한 사용자의 '좋아요'를 추가합니다.")
    @PostMapping("/product/{productId}")
    fun addLikeToProduct(
        @Parameter(description = "상품 ID") @PathVariable productId: Long,
        @AuthenticationPrincipal user: UserPrincipal,
    ): ResponseEntity<Void> { // void는 리스폰스 바디 가 없을때 씀
        likeService.addLikeToProduct(productId, user.id)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }
//    @Operation(summary = "상품에 '좋아요' 추가", description = "특정 상품에 대한 사용자의 '좋아요'를 추가합니다.")
//    @PostMapping("/product/{productId}/socialUser/{socialUserId}")
//    fun addLikeToProduct(
//        @Parameter(description = "상품 ID")@PathVariable productId: Long,
//        @Parameter(description = "소셜 사용자 ID")@PathVariable socialUserId: Long,
//    ) {
//        likeService.addLikeToProduct(productId, socialUserId)
//    }

    @Operation(summary = "상품에서 '좋아요' 제거", description = "특정 상품에 대한 사용자의 '좋아요'를 제거합니다.")
    @DeleteMapping("/product/{productId}")
    fun removeLikeFromProduct(
        @Parameter(description = "상품 ID") @PathVariable productId: Long,
        @AuthenticationPrincipal user: UserPrincipal,
    ): ResponseEntity<Void> {
        likeService.removeLikeFromProduct(productId, user.id)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    @Operation(summary = "리뷰에 '좋아요' 추가", description = "특정 리뷰에 대한 사용자의 '좋아요'를 추가합니다.")
    @PostMapping("/review/{reviewId}")
    fun addLikeToReview(
        @Parameter(description = "리뷰 ID") @PathVariable reviewId: Long,
        @AuthenticationPrincipal user: UserPrincipal,
    ): ResponseEntity<Void> {
        likeService.addLikeToReview(reviewId, user.id)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @Operation(summary = "리뷰에서 '좋아요' 제거", description = "특정 리뷰에 대한 사용자의 '좋아요'를 제거합니다.")
    @DeleteMapping("/review/{reviewId}")
    fun removeLikeFromReview(
        @Parameter(description = "리뷰 ID") @PathVariable reviewId: Long,
        @AuthenticationPrincipal user: UserPrincipal,
    ): ResponseEntity<Void> {
        likeService.removeLikeFromReview(reviewId, user.id)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }
}
