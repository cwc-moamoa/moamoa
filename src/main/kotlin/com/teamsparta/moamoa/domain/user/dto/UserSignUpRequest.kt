package com.teamsparta.moamoa.domain.user.dto

import com.teamsparta.moamoa.domain.user.model.User
import org.springframework.security.crypto.password.PasswordEncoder

data class UserSignUpRequest(
    val email: String,
    val password: String,
    val passwordCheck: String,
    var nickname: String,
    var address: String,
    var phoneNumber: String,
)

{

    fun toEntity(passwordEncoder: PasswordEncoder): User
    {
        return User(
            email = email,
            password = passwordEncoder.encode(password),
            nickname = nickname,
            address = address,
            phoneNumber = phoneNumber

        )
    }
}