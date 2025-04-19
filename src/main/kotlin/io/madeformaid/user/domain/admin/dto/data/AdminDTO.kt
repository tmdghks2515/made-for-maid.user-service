package io.madeformaid.user.domain.admin.dto.data

import io.madeformaid.shared.vo.enums.Role

data class AdminDTO(
        val id: String,
        val accountId: String,
        val email: String,
        val nickname: String,
        val roles: Set<Role>,
        val profileImageUrl: String?,
        val cafeId: String? = null,
)
