package io.madeformaid.user.domain.user.dto.command

data class UpdateUserProfileCommand(
        val nickname: String,
        val profileImageUrl: String? = null,
)
