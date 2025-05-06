package io.madeformaid.user.domain.admin.dto.data

import io.madeformaid.user.global.vo.StaffConcept
import io.madeformaid.user.global.vo.StaffType
import java.time.LocalDateTime

data class StaffDetailDTO(
    val userId: String,
    val shopId: String,
    val profileImageUrl: String?,
    val staffType: StaffType,
    val staffConcepts: Set<StaffConcept>?,
    val nickname: String,
    val introduction: String?,
    val approvedAt: LocalDateTime?,
)
