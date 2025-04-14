package io.made_for_maid.user_service.user.dto.data

import java.time.LocalDateTime

data class UserDTO(
        val id: String,
        val accountId: String,
        val email: String,
        val nickname: String,
        val currentMaidCafeId: String,
        val maidCafeIds: List<String>,
        val joinedAt: LocalDateTime,
        val accountJoinedAt: LocalDateTime,
)
