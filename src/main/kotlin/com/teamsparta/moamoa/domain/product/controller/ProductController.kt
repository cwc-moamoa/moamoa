package com.teamsparta.moamoa.domain.product.controller

import com.teamsparta.moamoa.domain.product.dto.ProductRequest
import com.teamsparta.moamoa.domain.product.dto.ProductResponse
import com.teamsparta.moamoa.domain.product.model.Product
import com.teamsparta.moamoa.domain.product.service.ProductService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/products")
class ProductController(
    private val productService: ProductService,
) {
    @GetMapping
    fun getAllProducts(): List<ProductResponse> {
        return productService.getAllProducts()
    }

    @GetMapping("/{productId}")
    fun getProduct(
        @PathVariable productId: Long,
    ): ProductResponse {
        return productService.getProductById(productId)
    }

    @PostMapping
    fun createProduct(
        @RequestBody request: ProductRequest,
    ): ResponseEntity<ProductResponse> {
        val product = productService.createProduct(request)
        val response = ProductResponse(product)
        return ResponseEntity.ok(response)
    }

    @PutMapping("/{productId}")
    fun updateProduct(
        @PathVariable productId: Long,
        @RequestBody productRequest: ProductRequest,
    ): ResponseEntity<ProductResponse> {
        val updatedProduct = productService.updateProduct(productId, productRequest)
        return ResponseEntity.ok(ProductResponse(updatedProduct))
    }


    @PutMapping("/{productId}/delete")
    fun deleteProduct(
        @PathVariable productId: Long,
    ): ResponseEntity<ProductResponse> {
        val product = productService.deleteProduct(productId)
        return ResponseEntity.ok(ProductResponse(product))
    }

    @GetMapping("/pages")
    fun getPaginatedProductList(
        @PageableDefault(size = 15, sort = ["id"]) pageable: Pageable,
    ): ResponseEntity<Page<Product>> {
        val products = productService.getPaginatedProductList(pageable)
        return ResponseEntity.status(HttpStatus.OK).body(products) // 페이징 아직 오류 해결못함
    }
}
