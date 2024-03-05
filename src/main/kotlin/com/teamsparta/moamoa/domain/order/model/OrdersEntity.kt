package com.teamsparta.moamoa.domain.order.model

import com.teamsparta.moamoa.domain.order.dto.ResponseOrderDto
import com.teamsparta.moamoa.domain.product.model.ProductEntity
import com.teamsparta.moamoa.domain.user.model.UserEntity
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "orders")
class OrdersEntity(

    @Column(name = "product_name")
    var productName:String,
    @Column(name = "total_price")
    var totalPrice:Int,
    @Column(name = "address")
    var address:String,
    @Column(name = "created_at")
    var createdAt:LocalDateTime,
    @Column(name = "discount")
    var discount:Double,
    @Column(name = "quantity")
    var quantity:Int,
    @ManyToOne
    @JoinColumn(name = "product_id")
    var productId:ProductEntity,
    @ManyToOne
    @JoinColumn(name = "user_id")
    var userId:UserEntity

){
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var ordersId:Long? = null

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    var status:OrdesStatus = OrdesStatus.COMPLETED


}
fun OrdersEntity.toResponse():ResponseOrderDto{
    return ResponseOrderDto(
        ordersId = ordersId!!,
        productName = productName,
        totalPrice = totalPrice,
        address = address,
        createdAt = createdAt,
        status = OrdesStatus.COMPLETED.toString(),
        discount = discount,
        quantity = quantity
    )
}