package com.teamsparta.moamoa.product.repository

import com.teamsparta.moamoa.product.model.ProductStock
import org.springframework.data.jpa.repository.JpaRepository

interface ProductStockRepository :
    JpaRepository<ProductStock, Long>
