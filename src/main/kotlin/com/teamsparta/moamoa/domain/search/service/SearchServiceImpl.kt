package com.teamsparta.moamoa.domain.search.service

import com.teamsparta.moamoa.domain.product.model.Product
import com.teamsparta.moamoa.domain.review.model.Review
import com.teamsparta.moamoa.domain.search.dto.ProductSearchResponse
import com.teamsparta.moamoa.domain.search.dto.ReviewSearchResponse
import com.teamsparta.moamoa.domain.search.model.SearchHistory
import com.teamsparta.moamoa.domain.search.repository.SearchHistoryRepository
import com.teamsparta.moamoa.domain.search.repository.SearchProductRepository
import com.teamsparta.moamoa.domain.search.repository.SearchReviewRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Sort


@Service
class SearchServiceImpl(
    private val searchProductRepository: SearchProductRepository,
    private val searchReviewRepository: SearchReviewRepository,
    private val searchHistoryRepository: SearchHistoryRepository
) : SearchService {
    @Transactional
    override fun searchProductsByLikes(pageable: Pageable): List<ProductSearchResponse> {
        val products = searchProductRepository.findAllByDeletedAtIsNullOrderByLikesDesc(pageable)
        return products.map { product ->
            ProductSearchResponse(
                productId = product.id!!,
                title = product.title,
                content = product.content,
                imageUrl = product.imageUrl,
                price = product.price,
                ratingAverage = product.ratingAverage,
                likes = product.likes,
            )
        }
    }

    @Transactional
    override fun searchReviewsByLikes(pageable: Pageable): List<ReviewSearchResponse> {
        val reviews = searchReviewRepository.findAllByDeletedAtIsNullOrderByLikesDesc(pageable)
        return reviews.map { review ->
            ReviewSearchResponse(
                reviewId = review.id!!,
                productId = review.product.id!!,
                title = review.title,
                content = review.content,
                imageUrl = review.imageUrl,
                name = review.name,
                likes = review.likes,
            )
        }
    }

    @Transactional
    override fun searchProducts(keyword: String, pageable: Pageable): Page<Product> {
        saveSearchHistory(keyword)//검색하면 히스토리에 저장된다는 뜻
        return searchProductRepository.findByTitleContaining(keyword, pageable)
    }

    @Transactional
    override fun searchReviews(keyword: String, pageable: Pageable): Page<Review> {
        saveSearchHistory(keyword)
        return searchReviewRepository.findReviewsByTitleContaining(keyword, pageable)
    }

    @Transactional
    override fun saveSearchHistory(keyword: String) {
        var searchHistory = searchHistoryRepository.findByKeyword(keyword)
        if (searchHistory != null) {// 키워드를 누르면 서치 히스토리에 가서 확인하는데 이미 존재하는 키워드라면,즉 널이 아니면 count를 증가

            searchHistory.count++
        } else {
            // 널이라면, 즉 새로운 키워드라면 SearchHistory 에 1을 추가
            searchHistory = SearchHistory(id = null, keyword = keyword, count = 1)
        }
        searchHistoryRepository.save(searchHistory)
    }


    @Transactional
    override fun getPopularKeywords(): List<SearchHistory> {
        return searchHistoryRepository.findAll(Sort.by(Sort.Direction.DESC, "count"))
    }
}




