package com.teamsparta.moamoa.domain.product.repository

import com.teamsparta.moamoa.domain.product.model.ProductEntity
import com.teamsparta.moamoa.domain.product.model.StockEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StockRepository : JpaRepository<StockEntity, Long> {
    fun findByProduct(findProduct: ProductEntity): StockEntity?
}
