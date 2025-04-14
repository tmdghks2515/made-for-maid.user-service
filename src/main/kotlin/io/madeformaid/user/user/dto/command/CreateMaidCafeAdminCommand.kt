package io.madeformaid.user.user.dto.command

data class CreateMaidCafeAdminCommand(
        val accountId: String,
        val nickname: String,
        val maidCafeId: String
)
