package com.teamsparta.moamoa.domain.user.service

import com.teamsparta.moamoa.domain.user.dto.UserResponse
import com.teamsparta.moamoa.domain.user.dto.UserSignInRequest
import com.teamsparta.moamoa.domain.user.dto.UserSignInResponse
import com.teamsparta.moamoa.domain.user.dto.UserSignUpRequest
import com.teamsparta.moamoa.domain.user.exception.InvalidCredentialException
import com.teamsparta.moamoa.domain.user.model.User
import com.teamsparta.moamoa.domain.user.repository.UserRepository
import com.teamsparta.moamoa.exception.ModelNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.IllegalArgumentException

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
): UserService {

    @Transactional
    override fun userSignUp(userSignUpRequest: UserSignUpRequest): UserResponse {
        if (userRepository.existsByEmail(userSignUpRequest.email))
            throw IllegalStateException("Email is already in use")
        if(userSignUpRequest.passwordCheck != userSignUpRequest.password) throw InvalidCredentialException()
        val user = userRepository.save(userSignUpRequest.toEntity(passwordEncoder))
        return UserResponse.toResponse(user)
    }



}