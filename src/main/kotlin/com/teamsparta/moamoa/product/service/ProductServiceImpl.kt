package com.teamsparta.moamoa.product.service

import com.teamsparta.moamoa.product.dto.ProductRequest
import com.teamsparta.moamoa.product.dto.ProductResponse
import com.teamsparta.moamoa.product.model.Product
import com.teamsparta.moamoa.product.repository.ProductRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ProductServiceImpl(private val productRepository: ProductRepository) : ProductService {

    override fun getAllProducts(): List<ProductResponse> {
        return productRepository.findAll()
            .map { ProductResponse(it) }
    }

    override fun getProductById(id: Long): ProductResponse {
        val product = productRepository.findById(id)
            .orElseThrow { RuntimeException("Product not found") }
        return ProductResponse(product)
    }
    override fun createProduct(request: ProductRequest): Product {
        val product = Product(
            title = request.title,
            content = request.content,
            imageUrl = request.imageUrl,
            price = request.price,
            purchaseAble = request.purchaseAble,
            deletedAt = request.deletedAt,
            likes = request.likes,  // 추가
            productDiscount = request.productDiscount,  // 추가
            ratingAverage = request.ratingAverage,  // 추가
            sellerId = request.sellerId,  // 추가
            userLimit = request.userLimit, // 추가된 필드
            discount = request.discount // 추가된 필드
        )
        return productRepository.save(product)
    }
    override fun updateProduct(id: Long, request: ProductRequest): Product {
        val product = productRepository.findById(id)
            .orElseThrow { RuntimeException("Product not found") }

        product.apply {
            title = request.title
            content = request.content
            imageUrl = request.imageUrl
            price = request.price
            purchaseAble = request.purchaseAble
            deletedAt = request.deletedAt
        }

        return productRepository.save(product)
    }

    override fun deleteProduct(id: Long): Product {
        val product = productRepository.findById(id)
            .orElseThrow { RuntimeException("Product not found") }

        product.deletedAt = LocalDateTime.now()

        return productRepository.save(product)
    }

}