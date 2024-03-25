package com.teamsparta.moamoa.domain.fixture

import com.teamsparta.moamoa.domain.socialUser.model.OAuth2Provider
import com.teamsparta.moamoa.domain.socialUser.model.SocialUser

class CreateSocialUserFixture {
    companion object {
        fun createSocialUser() : SocialUser {
            return SocialUser(
                id = 1L,
                email = "email",
                nickname = "nickname",
                provider = OAuth2Provider.KAKAO,
                providerId = "providerId"
            )
        }
    }
}