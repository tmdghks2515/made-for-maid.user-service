package io.madeformaid.user.user.dto.command

data class CreateUserCommand(
        val accountId: String,
        val nickname: String,
        val maidCafeId: String
)
