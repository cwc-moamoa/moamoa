package com.teamsparta.moamoa.domain.seller.dto

import com.teamsparta.moamoa.domain.seller.model.Seller
import org.springframework.security.crypto.password.PasswordEncoder

data class SellerSignUpRequest(
    val email: String,
    val password: String,
    val passwordCheck: String,
    var nickname: String,
    var address: String,
    var phoneNumber: String,
    var bizRegistrationNumber: String,
) {
    fun toEntity(passwordEncoder: PasswordEncoder): Seller {
        return Seller(
            email = email,
            password = passwordEncoder.encode(password),
            nickname = nickname,
            address = address,
            phoneNumber = phoneNumber,
            bizRegistrationNumber = bizRegistrationNumber,
        )
    }
}
