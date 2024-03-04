package com.teamsparta.moamoa.product.model

import com.teamsparta.moamoa.infra.BaseTimeEntity
import jakarta.persistence.*

@Entity
@Table(name = "product_stock")
data class ProductStock(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @OneToOne(mappedBy = "productStock", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val product: Product,

    @Column(name = "stock")
    val stock: Int,

    @Column(name = "product_name")
    val productName: String
): BaseTimeEntity()