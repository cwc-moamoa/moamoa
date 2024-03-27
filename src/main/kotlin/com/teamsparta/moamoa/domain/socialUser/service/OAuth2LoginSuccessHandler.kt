package com.teamsparta.moamoa.domain.socialUser.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.teamsparta.moamoa.domain.socialUser.JwtHelper
import com.teamsparta.moamoa.domain.socialUser.dto.OAuth2UserInfo
import com.teamsparta.moamoa.domain.socialUser.dto.SocialLoginResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping


@Controller
class OAuth2LoginSuccessHandler(
    private val jwtHelper: JwtHelper,
) : AuthenticationSuccessHandler {

    @PostMapping("oauth2/callback/kakao")
    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication,
    ) {
        val userInfo = authentication.principal as OAuth2UserInfo
        val accessToken = jwtHelper.generateAccessToken(userInfo.id, userInfo.nickname, userInfo.email, response)
        response.addHeader("Authorization", "Bearer $accessToken")
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.writer.write(jacksonObjectMapper().writeValueAsString(
            SocialLoginResponse.of(
                userInfo.id,
                userInfo.provider,
                userInfo.nickname,
                userInfo.email)
            )
        )
    }
}
