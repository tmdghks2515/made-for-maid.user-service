package io.madeformaid.user.utils

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import io.madeformaid.shared.config.AuthProperties
import io.madeformaid.user.admin.dto.data.AdminDTO
import io.madeformaid.user.user.dto.data.UserDTO
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.Instant
import java.util.Date

@Component
class JwtTokenProvider(
    private val authProperties: AuthProperties
) {
    private val key = Keys.hmacShaKeyFor(authProperties.jwt.secret.toByteArray())

    fun createAccessToken(user: UserDTO): String {
        val now = Date()
        val expiry = Date(now.time + authProperties.jwt.accessTokenExpireTime)

        return Jwts.builder()
                .setSubject(user.id)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact()
    }

    fun createAccessToken(admin: AdminDTO): String {
        val now = Date()
        val expiry = Date(now.time + authProperties.jwt.accessTokenExpireTime)

        return Jwts.builder()
                .setSubject(admin.id)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .claim("adminRole", admin.adminRole)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact()
    }

    fun createRefreshToken(userAdminId: String): String {
        val now = Date()
        val expiry = Date(now.time + authProperties.jwt.refreshTokenExpireTime)

        return Jwts.builder()
                .setSubject(userAdminId)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact()
    }
}
