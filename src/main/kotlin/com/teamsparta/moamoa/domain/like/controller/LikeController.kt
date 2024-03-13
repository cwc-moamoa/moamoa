// package com.teamsparta.moamoa.domain.like.controller
//
// import com.teamsparta.moamoa.like.service.LikeService
// import org.springframework.http.HttpStatus
// import org.springframework.http.ResponseEntity
// import org.springframework.web.bind.annotation.*
//
// @RestController
// @RequestMapping("/likes")
// class LikeController(private val likeService: LikeService) {
//
//    @PostMapping("/{productId}")
//    fun addLike(@PathVariable productId: Long, @RequestParam userId: Long): ResponseEntity<Void> {
//        likeService.addLike(productId, userId)
//        return ResponseEntity.status(HttpStatus.CREATED).build()
//    }
//
//    @DeleteMapping("/{productId}")
//    fun removeLike(@PathVariable productId: Long, @RequestParam userId: Long): ResponseEntity<Void> {
//        likeService.removeLike(productId, userId)
//        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
//    }
// }
