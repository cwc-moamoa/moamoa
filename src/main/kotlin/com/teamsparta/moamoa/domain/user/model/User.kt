package com.teamsparta.moamoa.domain.user.model

import com.teamsparta.moamoa.infra.BaseTimeEntity
import jakarta.persistence.*
import java.time.LocalDateTime

@Table(name = "app_user")
@Entity
class User(
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
) : BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

}
