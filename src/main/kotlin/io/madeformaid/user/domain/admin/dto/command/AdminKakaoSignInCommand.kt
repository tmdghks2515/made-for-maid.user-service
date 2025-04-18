package io.madeformaid.user.domain.admin.dto.command

data class AdminKakaoSignInCommand(
        val oauthCode: String,
        val email: String, // to be deleted
        val oauthId: String, // tobe deleted
)
