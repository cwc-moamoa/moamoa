package com.teamsparta.moamoa.domain.product.service

import com.teamsparta.moamoa.domain.product.dto.ProductRequest
import com.teamsparta.moamoa.domain.product.dto.ProductResponse
import com.teamsparta.moamoa.domain.product.model.Product
import com.teamsparta.moamoa.domain.product.model.ProductStock
import com.teamsparta.moamoa.domain.product.repository.ProductRepository
import com.teamsparta.moamoa.domain.product.repository.ProductStockRepository
import com.teamsparta.moamoa.domain.seller.repository.SellerRepository
import com.teamsparta.moamoa.exception.ModelNotFoundException
import com.teamsparta.moamoa.exception.OutOfStockException
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ProductServiceImpl(
    private val productRepository: ProductRepository,
    private val productStockRepository: ProductStockRepository,
    private val sellerRepository: SellerRepository,
) : ProductService {
    @Transactional
    override fun getAllProducts(): List<ProductResponse> {
        val products = productRepository.findAll().filter { it.deletedAt == null }
        return products.map { ProductResponse(it) }
    }

    @Transactional
    override fun getProductById(productId: Long): ProductResponse {
        val product =
            productRepository.findByIdAndDeletedAtIsNull(productId)
                .orElseThrow { ModelNotFoundException("Product", productId) }
        return ProductResponse(product)
    }

    @Transactional
    override fun createProduct(
        sellerId: Long,
        request: ProductRequest,
    ): Product {
        val seller =
            sellerRepository.findByIdOrNull(sellerId)
                ?: throw ModelNotFoundException("seller", sellerId)

        val product =
            Product(
                title = request.title,
                content = request.content,
                imageUrl = request.imageUrl,
                price = request.price,
                purchaseAble = request.purchaseAble,
//                likes = request.likes,
                productDiscount = request.productDiscount,
                ratingAverage = request.ratingAverage,
                userLimit = request.userLimit,
                discount = request.discount,
                seller = seller,
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
        sellerId: Long,
        request: ProductRequest,
    ): Product {
        val product =
            productRepository.findByIdAndDeletedAtIsNull(productId)
                .orElseThrow { ModelNotFoundException("Product", productId) }

        val seller =
            sellerRepository.findByIdOrNull(sellerId)
                ?: throw ModelNotFoundException("seller", sellerId)

        if (seller != product.seller) {
            throw IllegalArgumentException("권한이 없습니다")
        }

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
    override fun deleteProduct(
        productId: Long,
        sellerId: Long,
    ): Product {
        val product =
            productRepository.findByIdAndDeletedAtIsNull(productId)
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
    override fun getPaginatedProductList(pageable: Pageable): Page<ProductResponse> {
        return productRepository.findAllByDeletedAtIsNull(pageable)
    }
} // 되나?
