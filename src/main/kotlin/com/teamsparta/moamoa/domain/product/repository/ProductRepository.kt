package com.teamsparta.moamoa.domain.product.repository

import com.teamsparta.moamoa.domain.product.dto.ProductResponse
import com.teamsparta.moamoa.domain.product.model.Product
import com.teamsparta.moamoa.domain.seller.model.Seller
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository : JpaRepository<Product, Long>, CustomProductRepository {
    fun findByIdAndDeletedAtIsNull(productId: Long): Product?

    fun findAllByDeletedAtIsNull(pageable: Pageable): Page<ProductResponse>

    fun findBySellerId(sellerId:Long): List<Product>

    fun findBySellerIdAndDeletedAtIsNull(seller: Long): List<Product>
}
