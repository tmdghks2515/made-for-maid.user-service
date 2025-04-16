package io.madeformaid.user.admin.dto.command

data class CreateAdminCommand(
        val accountId: String,
        val nickname: String,
        val maidCafeId: String,
)
