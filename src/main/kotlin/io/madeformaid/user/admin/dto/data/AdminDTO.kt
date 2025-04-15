package io.madeformaid.user.admin.dto.data

import io.madeformaid.shared.vo.enums.AdminRole

data class AdminDTO(
        val id: String,
        val accountId: String,
        val email: String,
        val nickname: String,
        val adminRole: AdminRole,
        val profileImageUrl: String?,
        val maidCafeId: String? = null,
)
