package com.teamsparta.moamoa.domain.user.dto

data class UserSignInRequest(
    val email: String,
    val password: String,
)
