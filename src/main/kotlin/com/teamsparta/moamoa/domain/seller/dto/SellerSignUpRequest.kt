package com.teamsparta.moamoa.domain.seller.dto

import com.teamsparta.moamoa.domain.seller.model.Seller
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.Length
import org.springframework.security.crypto.password.PasswordEncoder

data class SellerSignUpRequest(
    @field:NotBlank(message = "이메일은 필수입력 항목입니다. ")
    @field:Email(message = "이메일 형식이 아닙니다. " )
    val email: String,
    @field:Length(min = 8, max = 15, message = "비밀번호는 8자 이상, 15자 이하여야 합니다. ")
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
