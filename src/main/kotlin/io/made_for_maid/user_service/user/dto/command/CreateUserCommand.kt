package io.made_for_maid.user_service.user.dto.command

data class CreateUserCommand(
        val accountId: String,
        val nickname: String,
        val maidCafeId: String
)
