package com.teamsparta.moamoa.infra.security

import com.teamsparta.moamoa.domain.socialUser.service.OAuth2LoginSuccessHandler
import com.teamsparta.moamoa.domain.socialUser.service.OAuth2UserService
import com.teamsparta.moamoa.infra.security.jwt.JwtAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val authenticationEntryPoint: AuthenticationEntryPoint,
    private val accessDeniedHandler: AccessDeniedHandler,
    private val oAuth2UserService: OAuth2UserService,
    private val oAuth2LoginSuccessHandler: OAuth2LoginSuccessHandler,
) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .httpBasic { it.disable() }
            .formLogin { it.disable() }
            .csrf { it.disable() }
            .headers { it.frameOptions { options -> options.sameOrigin() } }
            .authorizeHttpRequests {
                it.requestMatchers(
                    "/sellers",
                    "/sellers/signup",
                    "/sellers/signin",
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/oauth2/login",
                    "/oauth2/callback/**",
                    "/h2-console/**",
                ).permitAll()
                    .anyRequest().authenticated()
            }
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .exceptionHandling {
                it.authenticationEntryPoint(authenticationEntryPoint)
                it.accessDeniedHandler(accessDeniedHandler)
            }
            .oauth2Login { oauthConfig ->
                oauthConfig.authorizationEndpoint {
                    it.baseUri("/oauth2/login")
                }.redirectionEndpoint {
                    it.baseUri("/oauth2/callback/*")
                }.userInfoEndpoint {
                    it.userService(oAuth2UserService)
                }.successHandler(oAuth2LoginSuccessHandler)
            }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .build()
    }
}

/* @Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val authenticationEntrypoint: AuthenticationEntryPoint,
    private val accessDeniedHandler: AccessDeniedHandler,
) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .httpBasic { it.disable() }
            .formLogin { it.disable() }
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it.requestMatchers(
                    "/users",
                    "/users/signup",
                    "/users/signin",
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                ).permitAll()
                    .anyRequest().authenticated()
            }
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .exceptionHandling {
                it.authenticationEntryPoint(authenticationEntrypoint)
                it.accessDeniedHandler(accessDeniedHandler)
            }
            .build()
    }
}

*/
