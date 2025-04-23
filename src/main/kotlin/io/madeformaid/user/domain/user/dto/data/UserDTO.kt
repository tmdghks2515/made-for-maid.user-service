package io.madeformaid.user.domain.user.dto.data

import io.madeformaid.shared.vo.enums.Role

data class UserDTO(
        val id: String,
        val accountId: String,
        val email: String,
        val nickname: String,
        val shopId: String,
        val roles: Set<Role>,
        val profileImageUrl: String?,
)
