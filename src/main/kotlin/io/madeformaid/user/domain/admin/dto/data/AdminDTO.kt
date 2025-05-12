package io.madeformaid.user.domain.admin.dto.data

import io.madeformaid.shared.vo.enums.Role
import io.madeformaid.user.global.vo.StaffConcept
import io.madeformaid.user.global.vo.StaffType
import java.time.LocalDateTime

data class AdminDTO(
        val id: String,
        val accountId: String,
        val email: String,
        val nickname: String,
        val roles: Set<Role>,
        val primaryRole: Role,
        val profileImageId: String?,
        val profileImageUrl: String?,
        val shopId: String?,
        val staffType: StaffType?,
        val staffConcepts: Set<StaffConcept>?,
        val approvedAt: LocalDateTime?,
)
