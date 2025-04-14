package io.madeformaid.user.user.dto.command

data class SignUpCommand(
        val email: String,
        val oauthId: String,
)
