package com.teamsparta.moamoa.domain.product.repository

import com.teamsparta.moamoa.domain.product.model.ProductEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository : JpaRepository<ProductEntity, Long>
