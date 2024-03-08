package com.teamsparta.moamoa.domain.socialUser

/*
import com.teamsparta.moamoa.domain.socialUser.service.OAuth2LoginSuccessHandler
import com.teamsparta.moamoa.domain.socialUser.service.OAuth2UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain

@Configuration
class OAuth2LoginConfig(
    private val oAuth2UserService: OAuth2UserService,
    private val oAuth2LoginSuccessHandler: OAuth2LoginSuccessHandler
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http.formLogin { it.disable() }
            .httpBasic { it.disable() }
            .csrf { it.disable() }
            .cors { it.disable() }
            .headers { it.frameOptions { options -> options.sameOrigin() } }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }.oauth2Login { oauthConfig ->
                oauthConfig.authorizationEndpoint {
                    it.baseUri("/oauth2/login") // /oauth2/login/kakao
                }.redirectionEndpoint {
                    it.baseUri("/oauth2/callback/*") // /oauth2/callback/kakao
                }.userInfoEndpoint {
                    it.userService(oAuth2UserService)
                }.successHandler(oAuth2LoginSuccessHandler)
            }.build()
    }
}

*/