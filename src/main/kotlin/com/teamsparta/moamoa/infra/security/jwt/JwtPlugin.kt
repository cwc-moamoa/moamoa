package com.teamsparta.moamoa.infra.security.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.time.Duration
import java.time.Instant
import java.util.*

@Component
class JwtPlugin(
    @Value("\${auth.jwt.issuer}") private val issuer: String,
    @Value("\${auth.jwt.secret}") private val secret: String,
    @Value("\${auth.jwt.accessTokenExpirationHour}") private val accessTokenExpirationHour: Long,
) {
    fun validateToken(jwt: String): Result<Jws<Claims>> {
        return kotlin.runCatching {
            val key = Keys.hmacShaKeyFor(secret.toByteArray(StandardCharsets.UTF_8))
            Jwts.parser().verifyWith(key).build().parseSignedClaims(jwt)
        }
    }

    fun generateAccessTokenForSocialUser(
        subject: String,
        nickname: String,
        email: String,
        httpServletResponse: HttpServletResponse,
    ): String {
        val token = generateToken(subject, nickname, email, Duration.ofHours(accessTokenExpirationHour))
        addTokenToCookie(token, httpServletResponse)
        return token
    }

    fun generateAccessToken(
        subject: String,
        nickname: String,
        email: String,
    ): String {
        return generateToken(subject, nickname, email, Duration.ofHours(accessTokenExpirationHour))
    }

    private fun generateToken(
        subject: String,
        nickname: String,
        email: String,
        expirationPeriod: Duration,
    ): String {
        val claims: Claims =
            Jwts.claims()
                .add(mapOf("email" to email, "nickname" to nickname))
                .build()

        val key = Keys.hmacShaKeyFor(secret.toByteArray(StandardCharsets.UTF_8))
        val now = Instant.now()

        return Jwts.builder()
            .subject(subject)
            .issuer(issuer)
            .issuedAt(Date.from(now))
            .expiration(Date.from(now.plus(expirationPeriod)))
            .claims(claims)
            .signWith(key)
            .compact()
    }

    private fun addTokenToCookie(
        token: String,
        httpServletResponse: HttpServletResponse,
    ) {
        val cookie = Cookie("jwt_token", token)
        cookie.isHttpOnly = false // JavaScript 에서 쿠키에 접근하지 못하도록 설정
        cookie.maxAge = (accessTokenExpirationHour * 60 * 60 * 24 * 7).toInt() // 약 8.4일
        cookie.path = "/" // 쿠키 객체를 모든 경로에서 쓸 수 있게 설정함.
        httpServletResponse.addCookie(cookie) // 쿠키 객체를 리스폰스에 담아서 클라이언트에게 주기.
    }
}
