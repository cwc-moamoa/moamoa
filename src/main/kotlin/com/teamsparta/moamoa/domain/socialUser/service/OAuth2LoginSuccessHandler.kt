package com.teamsparta.moamoa.domain.socialUser.service

import com.teamsparta.moamoa.domain.socialUser.JwtHelper
import com.teamsparta.moamoa.domain.socialUser.dto.OAuth2UserInfo
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component

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
        val accessToken = jwtHelper.generateAccessToken(userInfo.id, userInfo.nickname, userInfo.email)
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.writer.write(accessToken)
    }
}
