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

    fun generateAccessToken(
        subject: String,
        nickname: String,
        email: String,
        response: HttpServletResponse
    ): String {
        val token = generateToken(subject, nickname, email, Duration.ofHours(accessTokenExpirationHour))
        addTokenToCookie(token,response)
        return token
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

    private fun addTokenToCookie(token: String, response: HttpServletResponse) {
        val cookie = Cookie("jwt_token", token)
        cookie.isHttpOnly = true
        cookie.maxAge = (accessTokenExpirationHour * 60 * 60 * 24 * 7).toInt() // 일주일
        cookie.path = "/"
        response.addCookie(cookie)
    }
}
