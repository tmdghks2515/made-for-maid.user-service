package io.madeformaid.user.admin.service

import io.madeformaid.shared.vo.enums.AdminRole
import io.madeformaid.shared.vo.enums.OauthProvider
import io.madeformaid.shared.vo.enums.SignInResStatus
import io.madeformaid.user.admin.dto.command.AdminKakaoSignInCommand
import io.madeformaid.user.admin.dto.data.AdminSignInResDTO
import io.madeformaid.user.user.entity.AccountEntity
import io.madeformaid.user.user.mapper.AdminMapper
import io.madeformaid.user.user.mapper.UserMapper
import io.madeformaid.user.user.repository.AccountRepository
import io.madeformaid.user.utils.JwtTokenProvider
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class AdminAuthService(
        private val accountRepository: AccountRepository,
        private val userMapper: UserMapper,
        private val adminMapper: AdminMapper,
        private val jwtTokenProvider: JwtTokenProvider,
        @Value("\${system.secret}") private val systemSecretKey: String,
) {
    fun adminKakaoSignIn(command: AdminKakaoSignInCommand): Pair<AdminSignInResDTO, String?> {
        val existingAccount = accountRepository.findByOauthIdAndOauthProvider(
                command.oauthId,
                OauthProvider.KAKAO
        )

        if (command.systemKeyForSystemAdminSignUp != null) {
            // 시스템 관리자 회원가입
            require(command.systemKeyForSystemAdminSignUp == systemSecretKey) {
                "시스템 키가 일치하지 않습니다."
            }

            val account = existingAccount?.run {
                require(this.getSystemAdmin() == null) {
                    "이미 시스템 관리자 계정이 존재합니다."
                }
                this
            } ?: run {
                val accountEntity = AccountEntity(
                        email = command.email,
                        oauthProvider = OauthProvider.KAKAO,
                        oauthId = command.oauthId,
                )
                accountRepository.save(accountEntity)
            }

            return AdminSignInResDTO(
                    status = SignInResStatus.SIGN_UP_SUCCESS,
                    accountId = account.id,
            ) to null

        } else if (command.maidCafeIdForMaidSignUp != null) {
            // 메이드 회원가입
            // id 체크

            val account = existingAccount?.run {
                require(
                        this.admins.none {
                            it.adminRole == AdminRole.MAID &&
                                    it.maidCafeId == command.maidCafeIdForMaidSignUp
                        }
                ) { "이미 해당 메이드 카페에 메이드 계정이 존재합니다." }
                this
            } ?: run {
                val accountEntity = AccountEntity(
                        email = command.email,
                        oauthProvider = OauthProvider.KAKAO,
                        oauthId = command.oauthId,
                )
                accountRepository.save(accountEntity)
            }

            return AdminSignInResDTO(
                    status = SignInResStatus.SIGN_UP_SUCCESS,
                    accountId = account.id,
            ) to null

        } else {
            // 일반 케이스
            existingAccount?.run {
                return existingAccount.getRecentSignedInAdmin()?.let { recentSignedInAdmin ->
                    val recentSignedInAdminDTO = adminMapper.entityToAdminDTO(recentSignedInAdmin)
                    // 정상 로그인
                    AdminSignInResDTO(
                            status = SignInResStatus.SIGN_IN_SUCCESS,
                            accessToken = jwtTokenProvider.createAccessToken(recentSignedInAdminDTO),
                            adminInfo = recentSignedInAdminDTO,
                    ) to jwtTokenProvider.createRefreshToken(recentSignedInAdminDTO.id)
                } ?: run {
                    // account 는 존재하지만 admin 최초 로그인 시도 (신규 제휴)
                    AdminSignInResDTO(
                            status = SignInResStatus.SIGN_UP_SUCCESS,
                            accountId = existingAccount.id,
                    ) to null
                }
            } ?: run {
                // 메이드 카페 관리자 회원가입 (신규 제휴)
                val accountEntity = AccountEntity(
                        email = command.email,
                        oauthProvider = OauthProvider.KAKAO,
                        oauthId = command.oauthId,
                )
                val createdAccount = accountRepository.save(accountEntity)

                return AdminSignInResDTO(
                        status = SignInResStatus.SIGN_UP_SUCCESS,
                        accountId = createdAccount.id,
                ) to null
            }
        }
    }
}
