package io.madeformaid.user.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.madeformaid.shared.config.AuthProperties
import io.madeformaid.user.domain.admin.dto.data.AdminDTO
import io.madeformaid.user.domain.user.dto.data.UserDTO
import org.springframework.stereotype.Component
import java.util.Date

@Component
class JwtTokenProvider(
        private val authProperties: AuthProperties
) {
    private val algorithm: Algorithm = Algorithm.HMAC256(authProperties.jwt.secret)

    fun createAccessToken(user: UserDTO): String {
        return generateToken(
                subject = user.id,
                roles = user.roles.map { it.name }.toSet(),
                expiresInMillis = authProperties.jwt.accessTokenExpireTime * 1000L
        )
    }

    fun createAccessToken(admin: AdminDTO): String {
        return generateToken(
                subject = admin.id,
                roles = admin.roles.map { it.name }.toSet(),
                expiresInMillis = authProperties.jwt.accessTokenExpireTime * 1000L
        )
    }

    fun createRefreshToken(userAdminId: String): String {
        return generateToken(
                subject = userAdminId,
                roles = emptySet(), // refresh token에는 roles 생략
                expiresInMillis = authProperties.jwt.refreshTokenExpireTime * 1000L
        )
    }

    private fun generateToken(subject: String, roles: Set<String>, expiresInMillis: Long): String {
        val now = Date()
        val expiry = Date(now.time + expiresInMillis)

        val builder = JWT.create()
                .withSubject(subject)
                .withIssuedAt(now)
                .withExpiresAt(expiry)

        if (roles.isNotEmpty()) {
            builder.withClaim("roles", roles.toList())
        }

        return builder.sign(algorithm)
    }
}
