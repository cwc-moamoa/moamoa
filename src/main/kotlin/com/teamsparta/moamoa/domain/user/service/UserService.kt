package com.teamsparta.moamoa.domain.user.service

import com.teamsparta.moamoa.domain.user.dto.UserResponse
import com.teamsparta.moamoa.domain.user.dto.UserSignInRequest
import com.teamsparta.moamoa.domain.user.dto.UserSignInResponse
import com.teamsparta.moamoa.domain.user.dto.UserSignUpRequest

interface UserService {
    fun userSignUp(userSignUpRequest: UserSignUpRequest): UserResponse

    fun userSignIn(userSignInRequest: UserSignInRequest): UserSignInResponse

    fun userDelete(userId: Long): UserResponse
}