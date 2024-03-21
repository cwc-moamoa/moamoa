package com.teamsparta.moamoa.domain.product.controller

import com.teamsparta.moamoa.domain.product.dto.ProductRequest
import com.teamsparta.moamoa.domain.product.dto.ProductResponse
import com.teamsparta.moamoa.domain.product.service.ProductService
import com.teamsparta.moamoa.infra.security.UserPrincipal
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/products")
class ProductController(
    private val productService: ProductService,
) {
    @Operation(summary = "모든 상품 조회", description = "시스템에 등록된 모든 상품의 목록을 조회합니다.")
    @GetMapping
    fun getAllProducts(): List<ProductResponse> {
        return productService.getAllProducts()
    }

    @Operation(summary = "상품 상세 조회", description = "주어진 상품 ID에 해당하는 상품의 상세 정보를 조회합니다.")
    @GetMapping("/{productId}")
    fun getProduct(
        @PathVariable productId: Long,
    ): ProductResponse {
        return productService.getProductById(productId)
    }

    @Operation(summary = "상품 등록", description = "새로운 상품을 시스템에 등록합니다.")
    @PostMapping("/products")
    fun createProduct(
        @AuthenticationPrincipal user: UserPrincipal,
        @RequestBody @Valid request: ProductRequest,
    ): ResponseEntity<ProductResponse> {
        val product = productService.createProduct(user.id, request)
        val response = ProductResponse(product)
        return ResponseEntity.ok(response)
    }

    @Operation(summary = "상품 정보 업데이트", description = "주어진 상품 ID에 해당하는 상품의 정보를 업데이트합니다.")
    @PutMapping("/{productId}")
    fun updateProduct(
        @AuthenticationPrincipal user: UserPrincipal,
        @Parameter(description = "상품 ID") @PathVariable productId: Long,
        @RequestBody @Valid productRequest: ProductRequest,
    ): ResponseEntity<ProductResponse> {
        val updatedProduct = productService.updateProduct(productId, user.id, productRequest)
        return ResponseEntity.ok(ProductResponse(updatedProduct))
    }

    @Operation(summary = "상품 삭제", description = "주어진 상품 ID에 해당하는 상품을 시스템에서 삭제합니다.")
    @PutMapping("/{productId}/delete")
    fun deleteProduct(
        @AuthenticationPrincipal user: UserPrincipal,
        @Parameter(description = "상품 ID") @PathVariable productId: Long,
    ): ResponseEntity<ProductResponse> {
        val product = productService.deleteProduct(productId, user.id)
        return ResponseEntity.ok(ProductResponse(product))
    }

    @Operation(summary = "페이지네이션을 통한 상품 목록 조회", description = "주어진 페이지네이션 정보에 따라 상품 목록을 조회합니다.")
    @GetMapping("/pages")
    fun getPaginatedProductList(
        @Parameter(description = "페이지네이션 정보") @PageableDefault(size = 15, sort = ["id"]) pageable: Pageable,
    ): ResponseEntity<Page<ProductResponse>> {
        val products = productService.getPaginatedProductList(pageable)
        return ResponseEntity.status(HttpStatus.OK).body(products)
    }
}
