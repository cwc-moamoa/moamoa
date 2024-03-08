package com.teamsparta.moamoa.domain.seller.model

import com.teamsparta.moamoa.domain.product.model.Product
import jakarta.persistence.*

@Entity
@Table(name = "seller")
class SellerEntity(
    @Column(name = "email")
    var email: String,
    @Column(name = "password")
    var password: String,
    @Column(name = "nickname")
    var nickname: String,
    @Column(name = "address")
    var address: String,
    @Column(name = "phone_number")
    var phoneNumber: String,
    @Column(name = "business_num")
    var businessNum: String,
    @OneToMany(mappedBy = "seller", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var products: MutableList<Product> = mutableListOf()
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}
