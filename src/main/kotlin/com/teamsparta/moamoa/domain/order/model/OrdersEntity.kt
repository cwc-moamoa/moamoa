package com.teamsparta.moamoa.domain.order.model

import com.teamsparta.moamoa.domain.order.dto.ResponseOrderDto
import com.teamsparta.moamoa.domain.product.model.ProductEntity
import com.teamsparta.moamoa.domain.user.model.UserEntity
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
//    @Column(name = "created_at")
//    var createdAt: LocalDateTime,
    @Column(name = "discount")
    var discount: Double,
    @Column(name = "quantity")
    var quantity: Int,
    @ManyToOne
    @JoinColumn(name = "product_id")
    var product: ProductEntity,
    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: UserEntity,
) : BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var orderId: Long? = null

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    var status: OrdersStatus = OrdersStatus.COMPLETED

//    @Column(name = "deleted_at")
//    var deletedAt: LocalDateTime? = null
}

fun OrdersEntity.toResponse(): ResponseOrderDto {
    return ResponseOrderDto(
        orderId = orderId!!,
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
