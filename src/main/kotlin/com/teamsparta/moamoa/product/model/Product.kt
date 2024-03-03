package com.teamsparta.moamoa.product.model

import com.teamsparta.moamoa.infra.BaseTimeEntity
import jakarta.persistence.*
import java.time.LocalDateTime


@Entity
@Table(name = "product")
data class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name ="sellerId",nullable = false)
    val sellerId: Long,

    @Column(name ="price",nullable = false)
    var price: Int,

    @Column(name ="title",nullable = false)
    var title: String,

    @Column(name ="content",nullable = false)
    var content: String,

    @Column(name ="purchaseAble",nullable = false)
    var purchaseAble: Boolean,

    @Column(name ="ratingAverage",nullable = true)
    val ratingAverage: Double,

    @Column(name ="imageUrl",nullable = true)
    var imageUrl: String,

    @Column(name ="productDiscount",nullable = true)
    val productDiscount: Double,

    @Column(name ="likes",nullable = true)
    val likes: Int,

//    @Column(name ="isDeleted",nullable = false)
//    var isDeleted: Boolean

    @Column(name ="deleted_at")
    var deletedAt: LocalDateTime? = null,

    @Column(name ="userLimit")
    val userLimit: Int,

    @Column(name ="discount")
    val discount: Double,

    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JoinColumn(name = "product_stock_id")
    val productStock: ProductStock? = null

): BaseTimeEntity()