package io.madeformaid.user.utils

import org.springframework.http.ResponseCookie
import org.springframework.stereotype.Component

@Component
class CookieProvider {
    val refreshTokenKey = "refreshToken"

    fun createRefreshTokenCookie(refreshToken: String): ResponseCookie {
        return ResponseCookie.from(refreshTokenKey, refreshToken)
                .httpOnly(true)
                .secure(true)
                .maxAge(7 * 24 * 60 * 60)
                .sameSite("None")
                .build()
    }

    fun clearRefreshTokenCookie(): ResponseCookie {
        return ResponseCookie.from(refreshTokenKey, "")
                .httpOnly(true)
                .secure(true)
                .maxAge(0)
                .sameSite("None")
                .build()
    }
}
