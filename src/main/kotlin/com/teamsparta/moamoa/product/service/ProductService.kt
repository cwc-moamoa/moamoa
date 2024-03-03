package com.teamsparta.moamoa.product.service

import com.teamsparta.moamoa.product.dto.ProductRequest
import com.teamsparta.moamoa.product.dto.ProductResponse
import com.teamsparta.moamoa.product.model.Product

interface ProductService {
    fun getAllProducts(): List<ProductResponse>
//    fun getProductById(id: Long): ProductResponse
    fun createProduct(request: ProductRequest): Product
    fun updateProduct(id: Long, request: ProductRequest): Product

    fun deleteProduct(id: Long): Product
    fun getProductById(id: Long): ProductResponse





}