package com.teamsparta.moamoa.domain.socialUser.service

import com.teamsparta.moamoa.domain.socialUser.dto.OAuth2UserInfo
import com.teamsparta.moamoa.domain.socialUser.model.OAuth2Provider
import com.teamsparta.moamoa.domain.socialUser.model.SocialUser
import com.teamsparta.moamoa.domain.socialUser.repository.SocialUserRepository
import org.springframework.stereotype.Service

@Service
class SocialUserService(
    private val socialUserRepository: SocialUserRepository,
) {

    // provider랑 providerId로 회원 검색하고, 없으면 회원가입
    fun registerIfAbsent(userInfo: OAuth2UserInfo): SocialUser {
        val provider = OAuth2Provider.valueOf(userInfo.provider)
        return if (!socialUserRepository.existsByProviderAndProviderId(provider, userInfo.providerId)) {
            val socialUser = SocialUser.ofKakao(userInfo.email, userInfo.providerId, userInfo.nickname, )
            socialUserRepository.save(socialUser)
        } else {
            socialUserRepository.findByProviderAndProviderId(provider, userInfo.providerId)
        }
    }

    fun existUser(provider: OAuth2Provider, providerId: String): Boolean {
        return socialUserRepository.existsByProviderAndProviderId(provider, providerId)
    }

    fun findUser(provider: OAuth2Provider, providerId: String, email: String): SocialUser {
        return socialUserRepository.findByProviderAndProviderId(provider, providerId)
    }




}
