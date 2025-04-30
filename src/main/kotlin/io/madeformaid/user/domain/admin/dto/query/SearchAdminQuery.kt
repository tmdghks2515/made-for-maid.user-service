package io.madeformaid.user.domain.admin.dto.query

import io.madeformaid.shared.vo.enums.Role
import io.madeformaid.user.global.vo.StaffType

data class SearchAdminQuery(
    val shopId: String? = null,
    val nicknameLike: String? = null,
    val primaryRoles: List<Role>? = null,
    val staffType: StaffType? = null,
)
