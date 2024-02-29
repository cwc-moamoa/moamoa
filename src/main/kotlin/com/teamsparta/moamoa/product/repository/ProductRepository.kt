package com.teamsparta.moamoa.product.repository

import com.teamsparta.moamoa.product.model.Product
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository : JpaRepository<Product, Long> {

}