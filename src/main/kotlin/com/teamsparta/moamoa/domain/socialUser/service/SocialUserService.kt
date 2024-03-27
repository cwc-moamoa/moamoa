package com.teamsparta.moamoa.domain.socialUser.service

import com.teamsparta.moamoa.domain.socialUser.dto.OAuth2UserInfo
import com.teamsparta.moamoa.domain.socialUser.model.OAuth2Provider
import com.teamsparta.moamoa.domain.socialUser.model.SocialUser
import com.teamsparta.moamoa.domain.socialUser.repository.SocialUserRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.net.URI

@Service
class SocialUserService(
    private val socialUserRepository: SocialUserRepository,
) {
    fun registerIfAbsent(userInfo: OAuth2UserInfo): SocialUser {
        val provider = OAuth2Provider.valueOf(userInfo.provider)
        return if (!socialUserRepository.existsByProviderAndProviderId(provider, userInfo.id)) {
            val socialUser = SocialUser.ofKakao(userInfo.id, userInfo.nickname, userInfo.email)
            socialUserRepository.save(socialUser)
        } else {
            socialUserRepository.findByProviderAndProviderId(provider, userInfo.id)
        }
    }

    fun getKakaoLoginPage(): String{
        return "http://localhost:8080/oauth2/login/kakao"
    }


}