package com.teamsparta.moamoa.domain.user.service

import com.teamsparta.moamoa.domain.user.dto.UserResponse
import com.teamsparta.moamoa.domain.user.dto.UserSignInRequest
import com.teamsparta.moamoa.domain.user.dto.UserSignInResponse
import com.teamsparta.moamoa.domain.user.dto.UserSignUpRequest
import com.teamsparta.moamoa.domain.user.exception.InvalidCredentialException
import com.teamsparta.moamoa.domain.user.repository.UserRepository
import com.teamsparta.moamoa.exception.ModelNotFoundException
import com.teamsparta.moamoa.infra.security.jwt.JwtPlugin
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.ui.Model
import java.time.LocalDateTime

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtPlugin: JwtPlugin
): UserService {

    @Transactional
    override fun userSignUp(userSignUpRequest: UserSignUpRequest): UserResponse {
        if (userRepository.existsByEmail(userSignUpRequest.email))
            throw IllegalStateException("Email is already in use")
        if(userSignUpRequest.passwordCheck != userSignUpRequest.password) throw InvalidCredentialException()
        val user = userRepository.save(userSignUpRequest.toEntity(passwordEncoder))
        return UserResponse.toResponse(user)
    }

    @Transactional
    override fun userSignIn(userSignInRequest: UserSignInRequest): UserSignInResponse {
        val user = userRepository.findByEmail(userSignInRequest.email) ?: throw ModelNotFoundException("User", null)
        if(!passwordEncoder.matches(userSignInRequest.password, user.password)) throw InvalidCredentialException()
        return UserSignInResponse(
            accessToken = jwtPlugin.generateAccessToken(
                subject = user.id.toString(),
                email = user.email,
            )
        )
    }

    override fun userDelete(userId: Long): UserResponse {
        val user = userRepository.findByIdOrNull(userId) ?: throw ModelNotFoundException("User", userId)

        if(userId != user.id) {
            throw InvalidCredentialException()
        }
        user.deletedAt = LocalDateTime.now()
        userRepository.save(user)

        return UserResponse.toResponse(user)

        //진행중입니다~~~!!



    }



}