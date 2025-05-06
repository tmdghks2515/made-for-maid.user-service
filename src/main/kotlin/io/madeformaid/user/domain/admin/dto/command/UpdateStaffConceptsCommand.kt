package io.madeformaid.user.domain.admin.dto.command

import io.madeformaid.user.global.vo.StaffConcept

data class UpdateStaffConceptsCommand(
    val userId: String,
    val staffConcepts: Set<StaffConcept>,
)
