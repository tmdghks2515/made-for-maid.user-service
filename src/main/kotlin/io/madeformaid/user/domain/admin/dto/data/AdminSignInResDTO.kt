package io.madeformaid.user.domain.admin.dto.data

import io.madeformaid.user.vo.SignInResStatus


data class AdminSignInResDTO(
        val status: SignInResStatus,
        val accountId: String? = null,
        val accessToken: String? = null,
        val admin: AdminDTO? = null,
)
