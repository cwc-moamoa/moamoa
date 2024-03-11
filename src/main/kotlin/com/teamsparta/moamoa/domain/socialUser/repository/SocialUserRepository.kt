package com.teamsparta.moamoa.domain.socialUser.repository

import com.teamsparta.moamoa.domain.socialUser.model.OAuth2Provider
import com.teamsparta.moamoa.domain.socialUser.model.SocialUser
import org.springframework.data.repository.CrudRepository

interface SocialUserRepository : CrudRepository<SocialUser, Long> {
    fun existsByProviderAndProviderId(
        provider: OAuth2Provider,
        id: String,
    ): Boolean

    fun findByProviderAndProviderId(
        provider: OAuth2Provider,
        id: String,
    ): SocialUser
}
