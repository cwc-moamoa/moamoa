package com.teamsparta.moamoa.product.repository

import com.teamsparta.moamoa.product.model.Product
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ProductRepository : JpaRepository<Product, Long>, CustomProductRepository {
    fun findByIdAndDeletedAtIsNull(productId: Long): Optional<Product>
    fun findAllByDeletedAtIsNull(pageable: Pageable): Page<Product>
}

