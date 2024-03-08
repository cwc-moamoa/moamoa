package com.teamsparta.moamoa.domain.like.service
//
// import com.teamsparta.moamoa.like.model.Like
// import com.teamsparta.moamoa.like.repository.LikeRepository
// import com.teamsparta.moamoa.domain.product.repository.ProductRepository
// import com.teamsparta.moamoa.domain.user.repository.UserRepository
// import org.springframework.stereotype.Service
//
// @Service
// class LikeServiceImpl(
//    private val userRepository: UserRepository,
//    private val productRepository: ProductRepository,
//    private val likeRepository: LikeRepository
// ) : LikeService {
//    override fun addLike(productId: Long, userId: Long) {
//        val user = userRepository.findById(userId).orElseThrow { NoSuchElementException("User not found") }
//        val product = productRepository.findById(productId).orElseThrow { NoSuchElementException("Product not found") }
//
//        val existingLike = likeRepository.findByUserAndProduct(user, product)
//        if (existingLike != null) {
//            return
//        }
//
//        val like = Like(user = user, product = product)
//        likeRepository.save(like)
//
//        product.likesCount += 1
//        productRepository.save(product)
//
//    }
//
//    override fun removeLike(productId: Long, userId: Long) {
//        val user = userRepository.findById(userId).orElseThrow { NoSuchElementException("User not found") }
//        val product = productRepository.findById(productId).orElseThrow { NoSuchElementException("Product not found") }
//
//        val like = likeRepository.findByUserAndProduct(user, product)
//        if (like == null) {
//            return
//        }
//
//        likeRepository.delete(like)
//
//        product.likesCount -= 1
//        productRepository.save(product)
//    }
// }
