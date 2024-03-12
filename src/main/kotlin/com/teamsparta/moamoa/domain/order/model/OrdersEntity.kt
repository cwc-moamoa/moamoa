package com.teamsparta.moamoa.domain.order.model

import com.teamsparta.moamoa.domain.order.dto.ResponseOrderDto
import com.teamsparta.moamoa.domain.product.model.Product
import com.teamsparta.moamoa.domain.socialUser.model.SocialUser
import com.teamsparta.moamoa.infra.BaseTimeEntity
import jakarta.persistence.*

@Entity
@Table(name = "orders")
class OrdersEntity(
    @Column(name = "product_name")
    var productName: String,
    @Column(name = "total_price")
    var totalPrice: Int,
    @Column(name = "address")
    var address: String,
    @Column(name = "discount")
    var discount: Double,
    @Column(name = "quantity")
    var quantity: Int,
    @ManyToOne
    @JoinColumn(name = "product_id")
    var product: Product,
    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: SocialUser,
) : BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    var status: OrdersStatus = OrdersStatus.COMPLETED
}

fun OrdersEntity.toResponse(): ResponseOrderDto {
    return ResponseOrderDto(
        orderId = id!!,
        productName = productName,
        totalPrice = totalPrice,
        address = address,
        createdAt = createdAt,
        updatedAt = updatedAt,
        status = status.toString(),
        discount = discount,
        quantity = quantity,
    )
}
