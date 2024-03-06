package com.teamsparta.moamoa.domain.user.service

import com.teamsparta.moamoa.domain.user.dto.UserResponse
import com.teamsparta.moamoa.domain.user.dto.UserSignInRequest
import com.teamsparta.moamoa.domain.user.dto.UserSignInResponse
import com.teamsparta.moamoa.domain.user.dto.UserSignUpRequest

interface UserService {
    fun signUpUser(userSignUpRequest: UserSignUpRequest): UserResponse

    fun signInUser(userSignInRequest: UserSignInRequest): UserSignInResponse

    fun deleteUser(userId: Long): UserResponse
}
