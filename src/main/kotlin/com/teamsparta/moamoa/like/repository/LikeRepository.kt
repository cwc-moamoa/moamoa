 package com.teamsparta.moamoa.like.repository

 import com.teamsparta.moamoa.like.model.Like
 import com.teamsparta.moamoa.product.model.Product
 import com.teamsparta.moamoa.review.model.Review
 import com.teamsparta.moamoa.user.model.User
 import org.springframework.data.jpa.repository.JpaRepository
 import java.util.*

 interface LikeRepository : JpaRepository<Like, Long> {
  fun findByProduct(product: Product): Like?
  fun findByReview(review: Review): Like?
 }
//    fun findByUserAndProduct(user: User, product: Product): Like