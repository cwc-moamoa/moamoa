package com.teamsparta.moamoa.domain.socialUser

import com.teamsparta.moamoa.infra.security.jwt.JwtPlugin
import org.springframework.stereotype.Component

@Component
class JwtHelper(
    private val jwtPlugin: JwtPlugin,
) {
    fun generateAccessToken(
        subject: String,
        nickname: String,
        email: String,
    ): String {
        return jwtPlugin.generateAccessToken(subject, nickname, email)
    }
}
