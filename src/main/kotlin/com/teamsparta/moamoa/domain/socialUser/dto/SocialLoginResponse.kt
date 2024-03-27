package com.teamsparta.moamoa.domain.socialUser.dto

data class SocialLoginResponse(
    val providerId: String,
    val provider: String,
    val nickname: String,
    val email: String

) {

    companion object {
        fun of(providerId: String,
               provider: String,
               nickname: String,
               email: String): SocialLoginResponse {
            return SocialLoginResponse(
                providerId = providerId,
                provider = provider,
                nickname = nickname,
                email = email
            )
        }
    }
}