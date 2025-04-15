package io.madeformaid.user.admin.dto.command

data class CreateMaidCommand(
        val accountId: String,
        val nickname: String,
        val maidCafeId: String
)
