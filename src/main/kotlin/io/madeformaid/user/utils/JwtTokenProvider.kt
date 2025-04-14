package io.madeformaid.user.utils

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import io.madeformaid.user.user.dto.data.MaidCafeAdminDTO
import io.madeformaid.user.user.dto.data.SystemAdminDTO
import io.madeformaid.user.user.dto.data.UserDTO
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.Date

@Component
class JwtTokenProvider(
    @Value("\${jwt.secret}") private val secretKey: String
) {
    private val key = Keys.hmacShaKeyFor(secretKey.toByteArray())

    fun createAccessToken(user: UserDTO): String {
        val now = Date()
        val expiry = Date(now.time + 30 * 60 * 1000) // 30분

        return Jwts.builder()
                .setSubject(user.id)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .claim("accountId", user.accountId)
                .claim("email", user.email)
                .claim("nickname", user.nickname)
                .claim("currentMaidCafeId", user.currentMaidCafeId)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact()
    }

    fun createAccessToken(systemAdmin: SystemAdminDTO): String {
        val now = Date()
        val expiry = Date(now.time + 30 * 60 * 1000) // 30분

        return Jwts.builder()
                .setSubject(systemAdmin.id)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .claim("accountId", systemAdmin.accountId)
                .claim("email", systemAdmin.email)
                .claim("nickname", systemAdmin.nickname)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact()
    }

    fun createAccessToken(maidCafeAdmin: MaidCafeAdminDTO): String {
        val now = Date()
        val expiry = Date(now.time + 30 * 60 * 1000) // 30분

        return Jwts.builder()
                .setSubject(maidCafeAdmin.id)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .claim("accountId", maidCafeAdmin.accountId)
                .claim("email", maidCafeAdmin.email)
                .claim("nickname", maidCafeAdmin.nickname)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact()
    }

    fun createRefreshToken(userId: String): String {
        val now = Date()
        val expiry = Date(now.time + 7 * 24 * 60 * 60 * 1000) // 7일

        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact()
    }
}
