package com.teamsparta.moamoa.product.model

import jakarta.persistence.*

@Entity
@Table(name = "product_stock")
data class ProductStock(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "product_id")
    val productId: Long,

    @Column(name = "stock")
    val stock: Int,

    @Column(name = "product_name")
    val productName: String
)