package com.teamsparta.moamoa.domain.user.dto

import com.teamsparta.moamoa.domain.user.model.User
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.Length
import org.springframework.security.crypto.password.PasswordEncoder

data class UserSignUpRequest(
    @field:NotBlank(message = "이메일 입력은 필수입니다. ")
    @field:Email(message = "이메일 형식이 아닙니다.")
    val email: String,
    @field:Length(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
    val password: String,
    val passwordCheck: String,
    var nickname: String,
    var address: String,
    var phoneNumber: String,
) {
    fun toEntity(passwordEncoder: PasswordEncoder): User {
        return User(
            email = email,
            password = passwordEncoder.encode(password),
            nickname = nickname,
            address = address,
            phoneNumber = phoneNumber,
        )
    }
}
