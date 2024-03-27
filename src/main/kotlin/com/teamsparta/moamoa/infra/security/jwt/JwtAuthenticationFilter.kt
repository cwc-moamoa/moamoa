package com.teamsparta.moamoa.infra.security.jwt

import com.teamsparta.moamoa.infra.security.UserPrincipal
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtPlugin: JwtPlugin,
) : OncePerRequestFilter() {
    companion object {
        private val BEARER_PATTERN = Regex("^Bearer (.+?)$")
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val jwt = request.getBearerToken()

        if (jwt != null) {
            jwtPlugin.validateToken(jwt)
                .onSuccess {
                    val userId = it.payload.subject.toLong()
                    val email = it.payload.get("email", String::class.java)

                    val userPrincipal = UserPrincipal(id = userId, email = email)

                    val details = WebAuthenticationDetailsSource().buildDetails(request)
                    val auth = JwtAuthenticationToken(userPrincipal, details)

                    SecurityContextHolder.getContext().authentication = auth
                }
        }
        response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer $jwt")
        filterChain.doFilter(request, response)
    }

    private fun HttpServletRequest.getBearerToken(): String? {
        val headerValue = this.getHeader(HttpHeaders.AUTHORIZATION) ?: return null
        return BEARER_PATTERN.find(headerValue)?.groupValues?.get(1)
    }
}
