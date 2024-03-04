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

    //단방향/ 참조 안할꺼임 ~
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "product_id")
//    var productId:ProductEntity
    // 시간은 나중ㅇ ㅔ한번에 넣기 BaseTimeEntity
){
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var odersId:Long? = null

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    var status:OrdesStatus = OrdesStatus.COMPLETED


}
fun OrdersEntity.toResponse():ResponseOrderDto{
    return ResponseOrderDto(
        ordersId = odersId!!,
        productName = productName,
        totalPrice = totalPrice,
        address = address,
        createdAt = createdAt,
        status = OrdesStatus.COMPLETED.toString(),
        discount = discount,
        quantity = quantity
    )
}