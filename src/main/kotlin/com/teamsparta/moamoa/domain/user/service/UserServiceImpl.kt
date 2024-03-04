package com.teamsparta.moamoa.domain.user.service

import com.teamsparta.moamoa.domain.user.dto.UserResponse
import com.teamsparta.moamoa.domain.user.dto.UserSignUpRequest
import com.teamsparta.moamoa.domain.user.model.User
import com.teamsparta.moamoa.domain.user.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
): UserService {

    @Transactional
    override fun userSignUp(userSignUpRequest: UserSignUpRequest): UserResponse {
        if (userRepository.existsByEmail(userSignUpRequest.email)) {
            throw IllegalStateException("Email is already in use")
        }
        if(userSignUpRequest.passwordCheck != userSignUpRequest.password) throw Exception("비밀번호와 비밀번호 확인이 일치하지 않습니다. ")
        val user = userRepository.save(userSignUpRequest.toEntity(passwordEncoder))
        return UserResponse.toResponse(user)

    }

}