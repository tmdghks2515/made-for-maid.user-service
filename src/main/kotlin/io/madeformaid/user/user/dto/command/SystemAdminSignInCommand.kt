package io.madeformaid.user.user.dto.command

data class SystemAdminSignInCommand(
        val oauthCode: String,
        val systemKey: String,
        val email: String, // to be deleted
        val oauthId: String, // tobe deleted
)
