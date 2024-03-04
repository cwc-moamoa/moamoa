package com.teamsparta.moamoa.domain.user.model

import jakarta.persistence.*

@Entity
@Table(name = "moa_user")
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id:Long,
    @Column(name = "email")
    var email:String,
    @Column(name = "password")
    var password:String,
    @Column(name = "nickname")
    var nickName:String,
    @Column(name = "address")
    var address:String,
    @Column(name = "phone_number")
    var phoneNumber:String,

) {
}