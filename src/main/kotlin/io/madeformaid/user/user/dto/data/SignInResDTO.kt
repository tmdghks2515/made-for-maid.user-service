package io.madeformaid.user.user.dto.data

import io.madeformaid.shared.vo.enums.SignInResStatus

data class SignInResDTO(
        val status: SignInResStatus,
        val accountId: String? = null,
        val accessToken: String? = null,
)
