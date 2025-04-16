package io.madeformaid.user.admin.dto.data

import io.madeformaid.shared.vo.enums.SignInResStatus

data class AdminSignInResDTO(
        val status: SignInResStatus,
        val accountId: String? = null,
        val accessToken: String? = null,
        val admin: AdminDTO? = null,
)
