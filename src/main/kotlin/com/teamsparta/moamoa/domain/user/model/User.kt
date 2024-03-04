package com.teamsparta.moamoa.domain.user.model

import com.teamsparta.moamoa.infra.BaseTimeEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Table(name = "app_user")
@Entity
class User(
    @Column(name = "nickname")
    var email: String,

    @Column(name = "password")
    var password: String,

    @Column(name = "nickname")
    var nickname: String,

    @Column(name = "address")
    var address: String,

    @Column(name = "phone_number")
    var phoneNumber: String,
): BaseTimeEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

}

