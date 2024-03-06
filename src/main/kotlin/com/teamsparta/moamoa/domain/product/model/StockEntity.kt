package com.teamsparta.moamoa.domain.product.model

import jakarta.persistence.*

@Entity
@Table(name = "stock")
class StockEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var stockId: Long,
    @Column(name = "stock")
    var stock: Int,
    @Column(name = "product_name")
    var productName: String,
    @OneToOne
    @JoinColumn(name = "product_id")
    var productId: ProductEntity,
) {
    companion object {
        fun StockEntity.discount(num: Int): StockEntity  {
            val discountNum = stock - num
            return StockEntity(
                stockId = stockId,
                stock = discountNum,
                productName = productName,
                productId = productId,
            )
        }
    }
}
