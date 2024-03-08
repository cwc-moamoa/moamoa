 package com.teamsparta.moamoa.like.controller

 import com.teamsparta.moamoa.like.dto.ProductLikeRequest
 import com.teamsparta.moamoa.like.dto.ReviewLikeRequest
 import com.teamsparta.moamoa.like.service.LikeService
 import org.springframework.http.HttpStatus
 import org.springframework.http.ResponseEntity
 import org.springframework.web.bind.annotation.*

 @RestController
 @RequestMapping("/likes")
 class LikeController(private val likeService: LikeService) {


     @PostMapping("/likes/product")
     fun addLikeToProduct(@RequestBody request: ProductLikeRequest) {
         likeService.addLikeToProduct(request.productId)
     }

     @DeleteMapping("/likes/product/{productId}")
     fun removeLikeFromProduct(@PathVariable productId: Long): ResponseEntity<Void> {
         likeService.removeLikeFromProduct(productId)
         return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
     }

     @PostMapping("/likes/review")
     fun addLikeToReview(@RequestBody request: ReviewLikeRequest) {
         likeService.addLikeToReview(request.reviewId)
     }


     @DeleteMapping("/likes/review/{reviewId}")
     fun removeLikeFromReview(@PathVariable reviewId: Long): ResponseEntity<Void> {
         likeService.removeLikeFromReview(reviewId)
         return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
     }


 }
//     @GetMapping("/users/{userId}/likes/products")
//     fun getLikedProducts(@PathVariable userId: Long): List<LikeResponse> {
//         return likeService.getLikedProducts(userId)
//     }
//     @GetMapping("/users/{userId}/likes/reviews")
//     fun getLikedReviews(@PathVariable userId: Long): List<LikeResponse> {
//         return likeService.getLikedReviews(userId)
//     }