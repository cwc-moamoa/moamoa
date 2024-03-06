package com.teamsparta.moamoa.product.service

import com.teamsparta.moamoa.product.dto.ProductRequest
import com.teamsparta.moamoa.product.dto.ProductResponse
import com.teamsparta.moamoa.product.model.Product
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ProductService {
    fun getAllProducts(): List<ProductResponse>

    fun createProduct(request: ProductRequest): Product

    fun updateProduct(
        productId: Long,
        request: ProductRequest,
    ): Product

    fun deleteProduct(productId: Long): Product

    fun getProductById(productId: Long): ProductResponse

// 재고감소임
    fun decreaseStock(
        productId: Long,
        quantity: Int,
    )

    fun getPaginatedProductList(pageable: Pageable): Page<Product>
}
