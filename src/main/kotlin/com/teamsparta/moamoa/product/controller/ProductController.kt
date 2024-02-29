package com.teamsparta.moamoa.product.controller

import com.teamsparta.moamoa.exception.ModelNotFoundException
import com.teamsparta.moamoa.product.dto.ProductRequest
import com.teamsparta.moamoa.product.dto.ProductResponse
import com.teamsparta.moamoa.product.model.Product
import com.teamsparta.moamoa.product.repository.ProductRepository
import com.teamsparta.moamoa.product.service.ProductService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/products")
class ProductController(
    private val productService: ProductService,
    private val productRepository: ProductRepository
) {
    @GetMapping
    fun getAllProducts(): List<ProductResponse> {
        return productService.getAllProducts()
    }

    @GetMapping("/{productId}")
    fun getProduct(@PathVariable productId: Long): ResponseEntity<Product> {
        val product = productRepository.findById(productId)
            .orElseThrow { ModelNotFoundException("Product", productId) }
        return ResponseEntity.ok(product)
    }

    @PostMapping
    fun createProduct(@RequestBody request: ProductRequest): ResponseEntity<ProductResponse> {
        val product = productService.createProduct(request)
        val response = ProductResponse(product)
        return ResponseEntity.ok(response)
    }
    @PutMapping("/{productId}")
    fun updateProduct(@PathVariable productId: Long, @RequestBody productRequest: ProductRequest): ResponseEntity<ProductResponse> {
        val updatedProduct = productService.updateProduct(productId, productRequest)
        return ResponseEntity.ok(ProductResponse(updatedProduct))
    }

    @PutMapping("/{id}/delete")
    fun deleteProduct(@PathVariable id: Long): ResponseEntity<ProductResponse> {
        val product = productService.deleteProduct(id)
        return ResponseEntity.ok(ProductResponse(product))
    }
}
