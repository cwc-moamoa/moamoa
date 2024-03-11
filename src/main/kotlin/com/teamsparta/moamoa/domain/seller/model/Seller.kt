package com.teamsparta.moamoa.domain.seller.model

import com.teamsparta.moamoa.infra.BaseTimeEntity
import jakarta.persistence.*

@Table(name = "seller")
@Entity
class Seller(
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
    @Column(name = "biz_reg_number")
    var bizRegistrationNumber: String

) : BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

}
