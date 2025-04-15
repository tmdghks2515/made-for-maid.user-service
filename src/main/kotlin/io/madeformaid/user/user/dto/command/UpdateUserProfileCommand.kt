package io.madeformaid.user.user.dto.command

data class UpdateUserProfileCommand(
        val nickname: String,
        val profileImageUrl: String? = null,
)
