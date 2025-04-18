package io.madeformaid.user.domain.admin.dto.command

data class CreateSystemAdminCommand(
        val accountId: String,
        val nickname: String,
        val systemSecret: String
)
