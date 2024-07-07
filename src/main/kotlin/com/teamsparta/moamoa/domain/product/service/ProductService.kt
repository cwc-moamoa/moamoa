package com.teamsparta.moamoa.domain.product.service

import com.teamsparta.moamoa.domain.product.dto.ProductRequest
import com.teamsparta.moamoa.domain.product.dto.ProductResponse
import com.teamsparta.moamoa.domain.product.model.Product
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ProductService {
    fun getAllProducts(): List<ProductResponse>

    fun createProduct(
        sellerId: Long,
        request: ProductRequest,
    ): Product

    fun updateProduct(
        productId: Long,
        sellerId: Long,
        request: ProductRequest,
    ): Product

    fun deleteProduct(
        productId: Long,
        sellerId: Long,
    ): Product

    fun getProductById(productId: Long): ProductResponse

    fun decreaseStock(
        productId: Long,
        quantity: Int,
    )

    fun getPaginatedProductList(pageable: Pageable): Page<ProductResponse>
}
