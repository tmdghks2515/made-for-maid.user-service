package io.made_for_maid.user_service.user.dto.command

data class SignUpCommand(
        val email: String,
        val oauthId: String,
)
