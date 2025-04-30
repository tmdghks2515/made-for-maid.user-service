package io.madeformaid.user.global.utils

import io.madeformaid.shared.config.AuthProperties
import org.springframework.http.ResponseCookie
import org.springframework.stereotype.Component
import java.security.AuthProvider
import java.time.Duration

@Component
class CookieProvider(
        private val authProperties: AuthProperties
) {
    fun createRefreshTokenCookie(refreshToken: String): ResponseCookie {
        return ResponseCookie.from(authProperties.jwt.refreshTokenName, refreshToken)
                .httpOnly(true)
                .secure(true)
                .maxAge(Duration.ofSeconds(authProperties.jwt.refreshTokenExpireTime))
                .sameSite("None")
                .build()
    }

    fun clearRefreshTokenCookie(): ResponseCookie {
        return ResponseCookie.from(authProperties.jwt.refreshTokenName, "")
                .httpOnly(true)
                .secure(true)
                .maxAge(0)
                .sameSite("None")
                .build()
    }
}
