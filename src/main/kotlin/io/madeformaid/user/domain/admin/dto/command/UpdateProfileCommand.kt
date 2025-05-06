package io.madeformaid.user.domain.admin.dto.command

data class UpdateProfileCommand(
    val userId: String,
    val nickname: String,
    val profileImageUrl: String?,
)
