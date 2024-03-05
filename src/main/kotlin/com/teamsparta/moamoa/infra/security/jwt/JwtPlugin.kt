package com.teamsparta.moamoa.infra.security.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.time.Duration
import java.time.Instant
import java.util.*

@Component
class JwtPlugin{
    companion object {
        const val ISSUER = "team.sparta.com"
        const val SECRET = "PO4c8z41Hia5gJG3oeuFJMRYBB4Ws4aZ"
        const val ACCESS_TOKEN_EXPIRATION_HOUR: Long = 168
    }

    fun validateToken(jwt: String): Result<Jws<Claims>> {
        return kotlin.runCatching {
            val key = Keys.hmacShaKeyFor(SECRET.toByteArray(StandardCharsets.UTF_8))
            // key로 서명을 검증하고, 만료시간을 체크
            Jwts.parser().verifyWith(key).build().parseSignedClaims(jwt)
        }
    }

    fun generateAccessToken(subject: String, email: String): String {
        return generateToken(subject, email, Duration.ofHours(ACCESS_TOKEN_EXPIRATION_HOUR))

    }

    private fun generateToken(subject: String, email: String, expirationPeriod: Duration): String {
        val claims: Claims = Jwts.claims()
            .add(mapOf("email" to email))
            .build()

        val key = Keys.hmacShaKeyFor(SECRET.toByteArray(StandardCharsets.UTF_8))
        val now = Instant.now()

        return Jwts.builder()
            .subject(subject)
            .issuer(ISSUER)
            .issuedAt(Date.from(now))
            .expiration(Date.from(now.plus(expirationPeriod)))
            .claims(claims)
            .signWith(key)
            .compact()
    }



}