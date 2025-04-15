package io.madeformaid.user.user.dto.command

data class UserKakaoSignInCommand(
        val oauthCode: String,
        val email: String, // to be deleted
        val oauthId: String, // tobe deleted
)
