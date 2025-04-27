package io.madeformaid.user.domain.admin.service

import io.madeformaid.user.vo.OauthProvider
import io.madeformaid.user.domain.admin.dto.command.*
import io.madeformaid.user.domain.admin.dto.data.AdminSignInResDTO
import io.madeformaid.user.domain.user.entity.AccountEntity
import io.madeformaid.user.domain.user.repository.AccountRepository
import io.madeformaid.user.vo.SignInResStatus
import io.madeformaid.user.utils.JwtTokenProvider
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class AdminAuthService(
        private val accountRepository: AccountRepository,
        private val jwtTokenProvider: JwtTokenProvider,
) {
    fun adminKakaoSignIn(command: AdminKakaoSignInCommand): Pair<AdminSignInResDTO, String?> {
        val existingAccount = accountRepository.findByOauthIdAndOauthProvider(
            command.oauthId,
            OauthProvider.KAKAO
        )

        if (existingAccount != null) {
            val existingAccountId = existingAccount.id ?: error("Account ID null")
            val existingAdmins = existingAccount.getAdmins()

            if (existingAdmins.isEmpty())
                return AdminSignInResDTO(
                    status = SignInResStatus.SIGN_UP_SUCCESS,
                    accessToken = jwtTokenProvider.createAccountAccessToken(existingAccountId),
                ) to jwtTokenProvider.createAccountRefreshToken(existingAccountId)

            return AdminSignInResDTO(
                status = SignInResStatus.PROFILE_SELECT,
                accessToken = jwtTokenProvider.createAccountAccessToken(existingAccountId),
            ) to jwtTokenProvider.createAccountRefreshToken(existingAccountId)
        }

        val createdAccount = accountRepository.save(
            AccountEntity(
                email = command.email,
                oauthProvider = OauthProvider.KAKAO,
                oauthId = command.oauthId,
            )
        )
        val createdAccountId = createdAccount.id ?: error("Account ID null")

        return AdminSignInResDTO(
            status = SignInResStatus.SIGN_UP_SUCCESS,
            accessToken = jwtTokenProvider.createAccountAccessToken(createdAccountId),
        ) to jwtTokenProvider.createAccountRefreshToken(createdAccountId)
    }
}
