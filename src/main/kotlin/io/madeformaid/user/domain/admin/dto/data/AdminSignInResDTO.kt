package io.madeformaid.user.domain.admin.dto.data

import io.madeformaid.user.vo.SignInResStatus


data class AdminSignInResDTO(
        val status: SignInResStatus,
        val admin: AdminDTO? = null,
        val accessToken: String? = null,
)
