//package com.teamsparta.moamoa.domain.search.service
//
//import com.teamsparta.moamoa.domain.product.repository.ProductRepository
//import com.teamsparta.moamoa.domain.review.repository.ReviewRepository
//import com.teamsparta.moamoa.domain.search.dto.ProductSearchResponse
//import com.teamsparta.moamoa.domain.search.dto.ReviewSearchResponse
//import jakarta.transaction.Transactional
//import org.springframework.stereotype.Service
//
//@Service
//class SearchServiceImpl(
//    private val productRepository: ProductRepository,
//    private val reviewRepository: ReviewRepository,
//) : SearchService {
//    @Transactional
//    override fun searchProductsByLikes(): List<ProductSearchResponse> {
//        val products = productRepository.findAllByDeletedAtIsNullOrderByLikesDesc()
//        return products.map { product ->
//            ProductSearchResponse(
//                productId = product.id!!,
//                title = product.title,
//                content = product.content,
//                imageUrl = product.imageUrl,
//                price = product.price,
//                ratingAverage = product.ratingAverage,
//                likes = product.likes,
//            )
//        }
//    }
//
//    @Transactional
//    override fun searchReviewsByLikes(): List<ReviewSearchResponse> {
//        val reviews = reviewRepository.findAllByDeletedAtIsNullOrderByLikesDesc()
//        return reviews.map { review ->
//            ReviewSearchResponse(
//                reviewId = review.id!!,
//                productId = review.product.id!!,
//                title = review.title,
//                content = review.content,
//                imageUrl = review.imageUrl,
//                name = review.name,
//                likes = review.likes,
//            )
//        }
//    }
//}
