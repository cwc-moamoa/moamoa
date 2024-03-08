package com.teamsparta.moamoa.domain.product.repository

import com.teamsparta.moamoa.domain.product.model.Product
import com.teamsparta.moamoa.domain.product.model.ProductStock
import org.springframework.data.jpa.repository.JpaRepository

interface ProductStockRepository :
    JpaRepository<ProductStock, Long> {
    fun findByProduct(product: Product): ProductStock?
}
