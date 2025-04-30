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
                subject = user.accountId,
                roles = user.roles.map { it.name }.toSet(),
                userId = user.id,
                shopId = user.shopId,
                expiresInMillis = authProperties.jwt.accessTokenExpireTime * 1000L
        )
    }

    fun createAccessToken(admin: AdminDTO): String {
        return generateToken(
                subject = admin.accountId,
                roles = admin.roles.map { it.name }.toSet(),
                userId = admin.id,
                shopId = admin.shopId,
                expiresInMillis = authProperties.jwt.accessTokenExpireTime * 1000L
        )
    }

    fun createRefreshToken(user: UserDTO): String {
        return generateToken(
                subject = user.accountId,
                userId = user.id,
                expiresInMillis = authProperties.jwt.refreshTokenExpireTime * 1000L
        )
    }

    fun createRefreshToken(admin: AdminDTO): String {
        return generateToken(
                subject = admin.accountId,
                userId = admin.id,
                expiresInMillis = authProperties.jwt.refreshTokenExpireTime * 1000L
        )
    }

    fun createAccountAccessToken(accountId: String): String {
        return generateToken(
                subject = accountId,
                expiresInMillis = authProperties.jwt.accessTokenExpireTime * 1000L
        )
    }

    fun createAccountRefreshToken(accountId: String): String {
        return generateToken(
            subject = accountId,
            expiresInMillis = authProperties.jwt.refreshTokenExpireTime * 1000L
        )
    }

    private fun generateToken(
        subject: String,
        userId: String? = null,
        shopId: String? = null,
        roles: Set<String>? = null,
        expiresInMillis: Long
    ): String {
        val now = Date()
        val expiry = Date(now.time + expiresInMillis)

        val builder = JWT.create()
                .withSubject(subject)
                .withIssuedAt(now)
                .withExpiresAt(expiry)

        if (roles?.isNotEmpty() == true) {
            builder
                    .withClaim("roles", roles.toList())
        }
        if (userId?.isNotBlank() == true) {
            builder
                    .withClaim("userId", userId)
        }
        if (shopId?.isNotBlank() == true) {
            builder
                    .withClaim("shopId", shopId)
        }

        return builder.sign(algorithm)
    }
}
