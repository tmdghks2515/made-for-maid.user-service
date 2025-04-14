package io.madeformaid.user.user.dto.command

data class CreateSystemAdminCommand(
    val accountId: String,
    val nickname: String,
)
