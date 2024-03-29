package com.teamsparta.moamoa.domain.socialUser

import com.teamsparta.moamoa.infra.security.jwt.JwtPlugin
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component

@Component
class JwtHelper(
    private val jwtPlugin: JwtPlugin,
) {
    fun generateAccessToken(
        subject: String,
        nickname: String,
        email: String,
        httpServletResponse: HttpServletResponse,
    ): String {
        return jwtPlugin.generateAccessTokenForSocialUser(subject, nickname, email, httpServletResponse)
    }
}
