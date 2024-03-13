package com.teamsparta.moamoa.domain.socialUser.service

import com.teamsparta.moamoa.domain.socialUser.dto.OAuth2UserInfo
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class OAuth2UserService(
    private val socialUserService: SocialUserService,
) : DefaultOAuth2UserService() {
    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val originUser = super.loadUser(userRequest)
        val provider = userRequest.clientRegistration.clientName // "KAKAO"
        return OAuth2UserInfo.of(provider, userRequest, originUser)
            .also { socialUserService.registerIfAbsent(it) }
    }
}
