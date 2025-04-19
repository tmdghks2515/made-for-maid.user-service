package io.madeformaid.user.domain.user.dto.command

data class CreateUserCommand(
        val accountId: String,
        val nickname: String,
        val cafeId: String
)
