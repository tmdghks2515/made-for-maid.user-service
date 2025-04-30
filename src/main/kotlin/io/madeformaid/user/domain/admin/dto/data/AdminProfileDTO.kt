package io.madeformaid.user.domain.admin.dto.data

import io.madeformaid.user.global.vo.StaffType

data class AdminProfileDTO(
        val userId: String,
        val nickname: String,
        val profileImageUrl: String?,
        val staffType: StaffType?,
        val shopId: String,
        var shopName: String? = null,
)
