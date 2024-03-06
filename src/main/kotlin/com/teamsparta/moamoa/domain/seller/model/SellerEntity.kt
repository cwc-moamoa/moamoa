package com.teamsparta.moamoa.domain.seller.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

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
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var sellerId: Long? = null
}
