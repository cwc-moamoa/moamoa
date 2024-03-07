package com.teamsparta.moamoa.product.model

import com.fasterxml.jackson.annotation.JsonBackReference
import com.teamsparta.moamoa.infra.BaseTimeEntity
import jakarta.persistence.*

@Entity
@Table(name = "product_stock")
data class ProductStock(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val stockId: Long? = null,
//    @OneToOne(mappedBy = "productStock", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
//    val product: Product,
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @JsonBackReference
    val product: Product,
    @Column(name = "stock")
    var stock: Int,
    @Column(name = "product_name")
    val productName: String,
) : BaseTimeEntity()