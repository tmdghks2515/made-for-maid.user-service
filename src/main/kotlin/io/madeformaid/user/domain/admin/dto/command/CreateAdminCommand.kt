package io.madeformaid.user.domain.admin.dto.command

data class CreateAdminCommand(
        val nickname: String,
        val shopId: String,
)
