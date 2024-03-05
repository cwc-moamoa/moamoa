package com.teamsparta.moamoa.product.service

import com.teamsparta.moamoa.exception.OutOfStockException
import com.teamsparta.moamoa.product.dto.ProductRequest
import com.teamsparta.moamoa.product.dto.ProductResponse
import com.teamsparta.moamoa.product.model.Product
import com.teamsparta.moamoa.product.model.ProductStock
import com.teamsparta.moamoa.product.repository.ProductRepository
import com.teamsparta.moamoa.product.repository.ProductStockRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ProductServiceImpl(
    private val productRepository: ProductRepository,
    private val productStockRepository: ProductStockRepository,
) : ProductService {
    override fun getAllProducts(): List<ProductResponse> {
        val products = productRepository.findAll().filter { it.deletedAt == null }
        return products.map { ProductResponse(it) }
    }

    override fun getProductById(id: Long): ProductResponse {
        val product =
            productRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow { RuntimeException("Product not found or has been deleted") }

        return ProductResponse(product)
    }

    override fun createProduct(request: ProductRequest): Product {
        val product =
            Product(
                title = request.title,
                content = request.content,
                imageUrl = request.imageUrl,
                price = request.price,
                purchaseAble = request.purchaseAble,
                likes = request.likes,
                productDiscount = request.productDiscount,
                ratingAverage = request.ratingAverage,
                sellerId = request.sellerId,
                userLimit = request.userLimit,
                discount = request.discount,
                // deletedAt = null 필요없을듯
            )

        val productStock =
            ProductStock(
                product = product,
                stock = request.stock, // 초기 재고 설정
                productName = request.title,
            )

        product.productStock = productStock

        productRepository.save(product)

        return product
    }

    override fun updateProduct(
        id: Long,
        request: ProductRequest,
    ): Product {
        val product =
            productRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow { RuntimeException("Product not found") }

        product.apply {
            title = request.title
            content = request.content
            imageUrl = request.imageUrl
            price = request.price
            purchaseAble = request.purchaseAble
//            deletedAt = request.deletedAt
        }

        return productRepository.save(product)
    }

    override fun deleteProduct(id: Long): Product {
        val product =
            productRepository.findById(id)
                .orElseThrow { RuntimeException("Product not found") }

        product.deletedAt = LocalDateTime.now()

        return productRepository.save(product)
    }
    // 재고??

    override fun decreaseStock(
        productId: Long,
        quantity: Int,
    ) {
        val product =
            productRepository.findById(productId)
                .orElseThrow { RuntimeException("Product not found") }

        val productStock =
            product.productStock
                ?: throw RuntimeException("ProductStock not found")

        if (productStock.stock < quantity) {
            throw OutOfStockException("Not enough stock")
        }

        productStock.stock -= quantity
        productStockRepository.save(productStock)

        // 재고가 0이면 매진 처리
        if (productStock.stock == 0) {
            product.isSoldOut = true
            productRepository.save(product)
        }
    }

    override fun getPaginatedProductList(pageable: Pageable): Page<Product> {
        return productRepository.findByPageable(pageable)
    }// 페이징 아직 오류 해결못함
}

//    override fun getProductById(id: Long): ProductResponse {
//        val product = productRepository.findById(id)
//            .orElseThrow { RuntimeException("Product not found") }
//        return ProductResponse(product)
//    }
