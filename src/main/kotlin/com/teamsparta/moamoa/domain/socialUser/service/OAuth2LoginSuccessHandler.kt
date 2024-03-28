package com.teamsparta.moamoa.domain.socialUser.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.teamsparta.moamoa.domain.socialUser.JwtHelper
import com.teamsparta.moamoa.domain.socialUser.dto.OAuth2UserInfo
import com.teamsparta.moamoa.domain.socialUser.dto.SocialLoginResponse
import com.teamsparta.moamoa.domain.socialUser.model.OAuth2Provider
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component
class OAuth2LoginSuccessHandler(
    private val jwtHelper: JwtHelper,
    private val socialUserService: SocialUserService
) : AuthenticationSuccessHandler {
    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        httpServletResponse: HttpServletResponse,
        authentication: Authentication,
    ) {
        val userInfo = authentication.principal as OAuth2UserInfo

        // 기존에 있던 회원인지 조회
        val existedUser = socialUserService.existUser(OAuth2Provider.valueOf(userInfo.provider), userInfo.providerId)

        // 있으면 검색한 정보로 토큰 발급하고
        if (existedUser) {
            val user = socialUserService.findUser(OAuth2Provider.valueOf(userInfo.provider), userInfo.providerId, userInfo.email)
            val accessToken = jwtHelper.generateAccessToken(
                user.providerId, user.nickname, user.email, httpServletResponse
            )
            httpServletResponse.addHeader("Authorization", "Bearer $accessToken") // 헤더에 담는 걸로 바꿈
            httpServletResponse.contentType = MediaType.APPLICATION_JSON_VALUE
            httpServletResponse.sendRedirect("/")

//        response.contentType = MediaType.APPLICATION_JSON_VALUE
//        response.writer.write(accessToken) // 본문 응답했었음

            // 없으면 가져온 정보 응답함
        } else {
            httpServletResponse.contentType = MediaType.APPLICATION_JSON_VALUE
            httpServletResponse.writer.write(
                jacksonObjectMapper().writeValueAsString(
                    SocialLoginResponse.of(
                        userInfo.email,
                        userInfo.provider,
                        userInfo.providerId,
                        userInfo.nickname
                    )
                )
            )
        }
    }
}
