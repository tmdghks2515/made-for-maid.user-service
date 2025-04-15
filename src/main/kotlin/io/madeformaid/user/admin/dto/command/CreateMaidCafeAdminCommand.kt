package io.madeformaid.user.admin.dto.command

data class CreateMaidCafeAdminCommand(
        val accountId: String,
        val nickname: String,
        val maidCafeId: String
)
