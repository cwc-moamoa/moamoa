package com.teamsparta.moamoa.domain.order.model

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
    @Column(name = "status")
    var status:String,
    @Column(name = "discount")
    var discount:Double,

    //단방향, 참조 안할꺼임 ~
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "product_id")
//    var productId:ProductEntity
){
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var odersId:Long? = null
}