package com.teamsparta.moamoa.domain.socialUser.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.teamsparta.moamoa.domain.socialUser.JwtHelper
import com.teamsparta.moamoa.domain.socialUser.dto.OAuth2UserInfo
import com.teamsparta.moamoa.domain.socialUser.dto.SocialLoginResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping

@Component
class OAuth2LoginSuccessHandler(
    private val jwtHelper: JwtHelper,
) : AuthenticationSuccessHandler {
    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication,
    ) {
        val userInfo = authentication.principal as OAuth2UserInfo
        val accessToken = jwtHelper.generateAccessToken(userInfo.id, userInfo.nickname, userInfo.email, response)
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.setHeader("Authorization", "Bearer $accessToken")
        // SocialLoginResponse 객체는 반환하지 않음
        // response.contentType = MediaType.APPLICATION_JSON_VALUE
        // response.writer.write(jacksonObjectMapper().writeValueAsString(
        //     SocialLoginResponse.of(
        //         userInfo.id,
        //         userInfo.provider,
        //         userInfo.nickname,
        //         userInfo.email)
        // ))
    }
}
