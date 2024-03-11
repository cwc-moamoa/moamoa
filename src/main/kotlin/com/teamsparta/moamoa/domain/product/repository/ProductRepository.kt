package com.teamsparta.moamoa.domain.product.repository

import com.teamsparta.moamoa.domain.product.dto.ProductResponse
import com.teamsparta.moamoa.domain.product.model.Product
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import com.teamsparta.moamoa.domain.seller.model.SellerEntity

interface ProductRepository : JpaRepository<Product, Long>, CustomProductRepository {
    fun findByIdAndDeletedAtIsNull(productId: Long): Optional<Product>
    fun findAllByDeletedAtIsNull(pageable: Pageable): Page<ProductResponse>
    fun findBySellerId(seller: SellerEntity): List<Product>
}

