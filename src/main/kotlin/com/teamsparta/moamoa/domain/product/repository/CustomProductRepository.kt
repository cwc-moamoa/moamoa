package com.teamsparta.moamoa.domain.product.repository

import com.teamsparta.moamoa.domain.product.model.Product
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface CustomProductRepository {
    fun findByPageable(pageable: Pageable): Page<Product>
}
