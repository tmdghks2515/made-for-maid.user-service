package io.madeformaid.user.domain.admin.dto.command

import io.madeformaid.user.vo.StaffConcept
import io.madeformaid.user.vo.StaffType

data class CreateAdminCommand(
        val nickname: String,
        val shopId: String,
)
