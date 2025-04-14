package io.madeformaid.user.user.service

import io.madeformaid.shared.vo.enums.OauthProvider
import io.madeformaid.shared.vo.enums.SignInResStatus
import io.madeformaid.user.user.dto.command.SignInCommand
import io.madeformaid.user.user.dto.command.SystemAdminSignInCommand
import io.madeformaid.user.user.dto.data.SignInResDTO
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
class SignInService(
        private val accountRepository: AccountRepository,
        private val userMapper: UserMapper,
        private val adminMapper: AdminMapper,
        private val jwtTokenProvider: JwtTokenProvider,
        @Value("\${system.secret}") private val systemSecretKey: String,
) {
    fun userkakaoSignIn(command: SignInCommand): Pair<SignInResDTO, String?> {
        return accountRepository.findByOauthIdAndOauthProvider(
                command.oauthId,
                OauthProvider.KAKAO
        )?.let { account ->
            account.getRecentSignedInUser()?.let {
                // 로그인
                val recentSignedInUserDTO = userMapper.entityToDTO(it)
                SignInResDTO(
                        status = SignInResStatus.SIGN_IN_SUCCESS,
                        accessToken = jwtTokenProvider.createAccessToken(recentSignedInUserDTO),
                ) to jwtTokenProvider.createRefreshToken(recentSignedInUserDTO.id)
            } ?: run {
                // account 는 존재하지만 user 가 존재하지 않는 경우
                SignInResDTO(
                        status = SignInResStatus.SIGN_UP_SUCCESS,
                        accountId = account.id,
                ) to null
            }
        } ?: run {
            // 회원가입
            val accountEntity = AccountEntity(
                    email = command.email,
                    oauthProvider = OauthProvider.KAKAO,
                    oauthId = command.oauthId,
            )

            SignInResDTO(
                    status = SignInResStatus.SIGN_UP_SUCCESS,
                    accountId = accountRepository.save(accountEntity).id ?: throw IllegalStateException("Registered Account ID cannot be null"),
            ) to null
        }
    }

    fun systemAdminKakaoSignIn(command: SystemAdminSignInCommand): Pair<SignInResDTO, String?> {
        require(command.systemKey == systemSecretKey) { "Invalid system key" }

        return accountRepository.findByOauthIdAndOauthProvider(
                command.oauthId,
                OauthProvider.KAKAO
        )?.let { account ->
            account.getSystemAdmin()?.let { systemAdmin ->
                // 로그인
                val systemAdminDTO = adminMapper.entityToSystemAdminDTO(systemAdmin)
                SignInResDTO(
                        status = SignInResStatus.SIGN_IN_SUCCESS,
                        accessToken = jwtTokenProvider.createAccessToken(systemAdminDTO),
                ) to jwtTokenProvider.createRefreshToken(systemAdminDTO.id)
            } ?: run {
                // account 는 존재하지만 system admin 존재하지 않는 경우
                SignInResDTO(
                        status = SignInResStatus.SIGN_UP_SUCCESS,
                        accountId = account.id,
                ) to null
            }
        } ?: run {
            // 시스템 관리자 회원가입
            val accountEntity = AccountEntity(
                    email = command.email,
                    oauthProvider = OauthProvider.KAKAO,
                    oauthId = command.oauthId,
            )

            SignInResDTO(
                    status = SignInResStatus.SIGN_UP_SUCCESS,
                    accountId = accountRepository.save(accountEntity).id ?: throw IllegalStateException("Registered Account ID cannot be null"),
            ) to null
        }
    }

    fun maidCafeAdminKakaoSignIn(command: SignInCommand): Pair<SignInResDTO, String?> {
        return accountRepository.findByOauthIdAndOauthProvider(
                command.oauthId,
                OauthProvider.KAKAO
        )?.let { account ->
            account.getRecentSignedInMaidCafeAdmin()?.let { maidCafeAdmin ->
                // 로그인
                val maidCafeAdminDTO = adminMapper.entityToMaidCafeAdminDTO(maidCafeAdmin)
                SignInResDTO(
                        status = SignInResStatus.SIGN_IN_SUCCESS,
                        accessToken = jwtTokenProvider.createAccessToken(maidCafeAdminDTO),
                ) to jwtTokenProvider.createRefreshToken(maidCafeAdminDTO.id)
            } ?: run {
                // account 는 존재하지만 maidcade admin 존재하지 않는 경우
                SignInResDTO(
                        status = SignInResStatus.SIGN_UP_SUCCESS,
                        accountId = account.id,
                ) to null
            }
        } ?: run {
            // 관리자 회원가입
            val accountEntity = AccountEntity(
                    email = command.email,
                    oauthProvider = OauthProvider.KAKAO,
                    oauthId = command.oauthId,
            )

            SignInResDTO(
                    status = SignInResStatus.SIGN_UP_SUCCESS,
                    accountId = accountRepository.save(accountEntity).id ?: throw IllegalStateException("Registered Account ID cannot be null"),
            ) to null
        }
    }
}
