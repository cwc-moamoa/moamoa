package com.teamsparta.moamoa.domain.user.dto

data class UserSignUpRequest(
    val email: String,
    val password: String,
    val passwordCheck: String,
    var nickname: String,
    var address: String,
    var phoneNumber: String,
)