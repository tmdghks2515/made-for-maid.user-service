package io.madeformaid.user.domain.admin.dto.command

import io.madeformaid.user.global.vo.StaffConcept
import io.madeformaid.user.global.vo.StaffType

data class CreateStaffCommand(
    val nickname: String,
    val shopId: String,
    val staffType: StaffType,
    val staffConcepts: Set<StaffConcept>,
)
