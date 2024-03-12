package com.teamsparta.moamoa.domain.product.repository

import com.teamsparta.moamoa.domain.product.dto.ProductResponse
import com.teamsparta.moamoa.domain.product.model.Product
import com.teamsparta.moamoa.domain.seller.model.Seller
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ProductRepository : JpaRepository<Product, Long>, CustomProductRepository {
    fun findByIdAndDeletedAtIsNull(productId: Long): Optional<Product>

    fun findAllByDeletedAtIsNull(pageable: Pageable): Page<ProductResponse>

    fun findBySellerId(seller: Seller): List<Product>

    fun findAllByDeletedAtIsNullOrderByLikesDesc(pageable: Pageable): List<Product> // 좋아요순 검색을 위해 추가함
}
