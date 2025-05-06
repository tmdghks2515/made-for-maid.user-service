package io.madeformaid.user.domain.admin.dto.command

data class UpdateStaffIntroductionCommand(
    val userId: String,
    val introduction: String?,
)
