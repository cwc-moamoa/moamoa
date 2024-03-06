package com.teamsparta.moamoa.domain.product.repository

import com.teamsparta.moamoa.domain.product.model.ProductEntity
import com.teamsparta.moamoa.domain.product.model.StockEntity
import org.springframework.data.jpa.repository.JpaRepository

interface StockRepository : JpaRepository<StockEntity, Long> {
    fun findByProductId(findProduct: ProductEntity): StockEntity?
}
