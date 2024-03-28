package com.teamsparta.moamoa.domain.socialUser.dto

data class SocialLoginResponse(
    val email: String,
    val provider: String,
    val providerId: String,
    val nickname: String,
) {

    companion object {
        fun of(email: String, provider: String, providerId: String, nickname: String): SocialLoginResponse {
            return SocialLoginResponse(
                email = email,
                provider = provider,
                providerId = providerId,
                nickname = nickname
            )
        }
    }
}

