package com.teamsparta.moamoa.domain.user.service

import com.teamsparta.moamoa.domain.user.dto.UserResponse
import com.teamsparta.moamoa.domain.user.dto.UserSignUpRequest

interface UserService {
    fun userSignUp(userSignUpRequest: UserSignUpRequest): UserResponse
}