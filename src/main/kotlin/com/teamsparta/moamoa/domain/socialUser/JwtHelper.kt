package com.teamsparta.moamoa.domain.socialUser

import org.springframework.stereotype.Component

@Component
class JwtHelper {

    fun generateAccessToken(id: String): String {
        return "SampleAccessToken $id"
    }
}