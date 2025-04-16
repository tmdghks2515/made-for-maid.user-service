package io.madeformaid.user.utils

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import io.madeformaid.user.admin.dto.data.AdminDTO
import io.madeformaid.user.user.dto.data.UserDTO
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.Instant
import java.util.Date

@Component
class JwtTokenProvider(
    @Value("\${jwt.secret}") private val secretKey: String
) {
    private val key = Keys.hmacShaKeyFor(secretKey.toByteArray())

    fun createAccessToken(user: UserDTO): String {
        val now = Date()
        val expiry = Date.from(Instant.now().plus(Duration.ofMinutes(30)))

        return Jwts.builder()
                .setSubject(user.id)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact()
    }

    fun createAccessToken(admin: AdminDTO): String {
        val now = Date()
        val expiry = Date(now.time + 30 * 60 * 1000) // 30분

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
        val expiry = Date.from(Instant.now().plus(Duration.ofDays(180)))

        return Jwts.builder()
                .setSubject(userAdminId)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact()
    }
}
