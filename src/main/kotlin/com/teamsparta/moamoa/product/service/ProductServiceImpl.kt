package com.teamsparta.moamoa.product.service

import com.teamsparta.moamoa.exception.ModelNotFoundException
import com.teamsparta.moamoa.exception.OutOfStockException
import com.teamsparta.moamoa.product.dto.ProductRequest
import com.teamsparta.moamoa.product.dto.ProductResponse
import com.teamsparta.moamoa.product.model.Product
import com.teamsparta.moamoa.product.model.ProductStock
import com.teamsparta.moamoa.product.repository.ProductRepository
import com.teamsparta.moamoa.product.repository.ProductStockRepository
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ProductServiceImpl(
    private val productRepository: ProductRepository,
    private val productStockRepository: ProductStockRepository,
) : ProductService {
    @Transactional


    @Transactional
    override fun getProductById(productId: Long): ProductResponse {
        val product =
            productRepository.findByProductIdAndDeletedAtIsNull(productId)
                .orElseThrow { ModelNotFoundException("Product", productId) }
        return ProductResponse(product)
    }

    @Transactional
    override fun createProduct(request: ProductRequest): Product {
        val product =
            Product(
                title = request.title,
                content = request.content,
                imageUrl = request.imageUrl,
                price = request.price,
                purchaseAble = request.purchaseAble,
//                likesCount  = request.likes,
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
                stock = request.stock,
                productName = request.title,
            )

        product.productStock = productStock

        productRepository.save(product)

        return product
    }

    @Transactional
    override fun updateProduct(
        productId: Long,
        request: ProductRequest,
    ): Product {
        val product =
            productRepository.findByProductIdAndDeletedAtIsNull(productId)
                .orElseThrow { ModelNotFoundException("Product", productId) }


        product.apply {
            title = request.title
            content = request.content
            imageUrl = request.imageUrl
            price = request.price
            purchaseAble = request.purchaseAble
        }

        return productRepository.save(product)
    }


    @Transactional
    override fun deleteProduct(productId: Long): Product {
        val product =
            productRepository.findByProductIdAndDeletedAtIsNull(productId)
                .orElseThrow { ModelNotFoundException("Product", productId) }

        product.deletedAt = LocalDateTime.now()

        return productRepository.save(product)
    }

    @Transactional
    override fun decreaseStock(
        productId: Long,
        quantity: Int,
    ) {
        val product =
            productRepository.findById(productId)
                .orElseThrow { ModelNotFoundException("Product", productId) }

        val productStock =
            product.productStock
                ?: throw RuntimeException("ProductStock not found")

        if (productStock.stock < quantity) {
            throw OutOfStockException("Not enough stock")
        }

        productStock.stock -= quantity
        productStockRepository.save(productStock)

        if (productStock.stock == 0) {
            product.isSoldOut = true
            productRepository.save(product)
        }
    }

    @Transactional
    override fun getPaginatedProductList(pageable: Pageable): Page<Product> {
        return productRepository.findByPageable(pageable)
    }
} // 되나?
