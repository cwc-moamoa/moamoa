package com.teamsparta.moamoa.domain.user.dto

data class UserResponse(
    val email: String,
    val nickname: String,
    var address: String,
    var phoneNumber: String,
    var order: Long,
)
