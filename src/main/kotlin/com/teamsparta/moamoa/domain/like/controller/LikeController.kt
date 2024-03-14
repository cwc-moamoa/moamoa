package com.teamsparta.moamoa.domain.like.controller
//
// import com.teamsparta.moamoa.domain.like.service.LikeService
// import org.springframework.http.HttpStatus
// import org.springframework.http.ResponseEntity
// import org.springframework.web.bind.annotation.*
//
// @RestController
// @RequestMapping("/likes")
// class LikeController(private val likeService: LikeService) {
//    @PostMapping("/product/{productId}/user/{userId}")
//    fun addLikeToProduct(
//        @PathVariable productId: Long,
//        @PathVariable userId: Long,
//    ) {
//        likeService.addLikeToProduct(productId, userId)
//    }
//
//    @DeleteMapping("/product/{productId}/user/{userId}")
//    fun removeLikeFromProduct(
//        @PathVariable productId: Long,
//        @PathVariable userId: Long,
//    ): ResponseEntity<String> {
//        likeService.removeLikeFromProduct(productId, userId)
//        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
//    }
//
//    @PostMapping("/review/{reviewId}/user/{userId}")
//    fun addLikeToReview(
//        @PathVariable reviewId: Long,
//        @PathVariable userId: Long,
//    ) {
//        likeService.addLikeToReview(reviewId, userId)
//    }
//
//    @DeleteMapping("/review/{reviewId}/user/{userId}")
//    fun removeLikeFromReview(
//        @PathVariable reviewId: Long,
//        @PathVariable userId: Long,
//    ): ResponseEntity<String> {
//        likeService.removeLikeFromReview(reviewId, userId)
//        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
//    }
// }
