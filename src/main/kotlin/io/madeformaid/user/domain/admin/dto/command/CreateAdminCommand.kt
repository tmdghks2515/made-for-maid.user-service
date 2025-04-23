package io.madeformaid.user.domain.admin.dto.command

data class CreateAdminCommand(
        val accountId: String,
        val nickname: String,
        val shopId: String,
)
