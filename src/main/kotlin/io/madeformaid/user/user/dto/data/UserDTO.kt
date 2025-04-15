package io.madeformaid.user.user.dto.data

data class UserDTO(
        val id: String,
        val accountId: String,
        val email: String,
        val nickname: String,
        val maidCafeId: String,
        val profileImageUrl: String?,
)
