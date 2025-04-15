package io.madeformaid.user.admin.dto.command

data class CreateSystemAdminCommand(
    val accountId: String,
    val nickname: String,
)
