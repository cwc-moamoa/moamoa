package com.teamsparta.moamoa.domain.user.dto

import com.teamsparta.moamoa.domain.user.model.User

data class UserResponse(
    val id: Long,
    val email: String,
    val nickname: String,
    var address: String,
    var phoneNumber: String,
) {
    companion object {
        fun toResponse(user: User): UserResponse  {
            return UserResponse(
                user.id!!,
                user.email,
                user.nickname,
                user.address,
                user.phoneNumber,
            )
        }
    }
}
