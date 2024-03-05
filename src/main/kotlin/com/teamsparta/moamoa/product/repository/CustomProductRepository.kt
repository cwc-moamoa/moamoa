package com.teamsparta.moamoa.product.repository

import com.teamsparta.moamoa.product.model.Product
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface CustomProductRepository {
    fun findByPageable(pageable: Pageable): Page<Product>// 페이징 아직 오류 해결못함
}
