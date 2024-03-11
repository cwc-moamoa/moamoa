package com.teamsparta.moamoa.domain.socialUser.dto

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import java.lang.RuntimeException

data class OAuth2UserInfo(
    val id: String,
    val provider: String,
    val nickname: String,
    val email: String,
) : OAuth2User {
    override fun getName(): String {
        return "$provider:$id"
    }

    override fun getAttributes(): MutableMap<String, Any> {
        return mutableMapOf()
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf()
    }

    companion object {
        fun of(
            provider: String,
            userRequest: OAuth2UserRequest,
            originUser: OAuth2User,
        ): OAuth2UserInfo {
            return when (provider) {
                "KAKAO", "kakao" -> ofKakao(provider, userRequest, originUser)
                else -> throw RuntimeException("지원하지 않는 OAuth Provider 입니다.")
            }
        }

        private fun ofKakao(
            provider: String,
            userRequest: OAuth2UserRequest,
            originUser: OAuth2User,
        ): OAuth2UserInfo {
            val profile = originUser.attributes["properties"] as Map<*, *>
            val userNameAttributeName =
                userRequest.clientRegistration.providerDetails.userInfoEndpoint.userNameAttributeName
            val nickname = profile["nickname"] ?: ""
            val account = originUser.attributes["kakao_account"] as Map<*, *>
            val email = account["email"] ?: ""

            return OAuth2UserInfo(
                id = (originUser.attributes[userNameAttributeName] as Long).toString(),
                provider = provider.uppercase(),
                nickname = nickname as String,
                email = email as String,
            )
        }
    }
}
