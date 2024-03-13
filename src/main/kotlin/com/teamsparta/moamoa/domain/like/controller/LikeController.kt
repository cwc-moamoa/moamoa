package com.teamsparta.moamoa.domain.like.controller

import com.teamsparta.moamoa.domain.like.service.LikeService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/likes")
class LikeController(private val likeService: LikeService) {
    @PostMapping("/product/{productId}/socialUser/{socialUserId}")
    fun addLikeToProduct(
        @PathVariable productId: Long,
        @PathVariable socialUserId: Long,
    ) {
        likeService.addLikeToProduct(productId, socialUserId)
    }

    @DeleteMapping("/product/{productId}/socialUser/{socialUserId}")
    fun removeLikeFromProduct(
        @PathVariable productId: Long,
        @PathVariable socialUserId: Long,
    ): ResponseEntity<String> {
        likeService.removeLikeFromProduct(productId, socialUserId)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    @PostMapping("/review/{reviewId}/socialUser/{socialUserId}")
    fun addLikeToReview(
        @PathVariable reviewId: Long,
        @PathVariable socialUserId: Long,
    ) {
        likeService.addLikeToReview(reviewId, socialUserId)
    }

    @DeleteMapping("/review/{reviewId}/socialUser/{socialUserId}")
    fun removeLikeFromReview(
        @PathVariable reviewId: Long,
        @PathVariable socialUserId: Long,
    ): ResponseEntity<String> {
        likeService.removeLikeFromReview(reviewId, socialUserId)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }
}
