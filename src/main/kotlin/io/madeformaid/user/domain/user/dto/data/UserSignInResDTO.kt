package io.madeformaid.user.domain.user.dto.data

import io.madeformaid.shared.vo.enums.SignInResStatus

data class UserSignInResDTO(
        val status: SignInResStatus,
        val accountId: String? = null,
        val accessToken: String? = null,
        val user: UserDTO? = null,
)
