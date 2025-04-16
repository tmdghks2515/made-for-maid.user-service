package io.madeformaid.user.user.service

import io.madeformaid.shared.vo.enums.OauthProvider
import io.madeformaid.shared.vo.enums.SignInResStatus
import io.madeformaid.user.user.dto.command.CreateUserCommand
import io.madeformaid.user.user.dto.command.UserKakaoSignInCommand
import io.madeformaid.user.user.dto.data.CreateUserResDTO
import io.madeformaid.user.user.dto.data.UserSignInResDTO
import io.madeformaid.user.user.entity.AccountEntity
import io.madeformaid.user.user.entity.UserEntity
import io.madeformaid.user.user.mapper.UserMapper
import io.madeformaid.user.user.repository.AccountRepository
import io.madeformaid.user.user.repository.UserRepository
import io.madeformaid.user.utils.JwtTokenProvider
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserAuthService(
        private val accountRepository: AccountRepository,
        private val userRepository: UserRepository,
        private val userMapper: UserMapper,
        private val jwtTokenProvider: JwtTokenProvider,
        @Value("\${system.secret}") private val systemSecretKey: String,
) {
    fun kakaoSignIn(command: UserKakaoSignInCommand): Pair<UserSignInResDTO, String?> {
        return accountRepository.findByOauthIdAndOauthProvider(
                command.oauthId,
                OauthProvider.KAKAO
        )?.let { account ->
            account.getRecentSignedInUser()?.let {
                // 로그인
                val recentSignedInUserDTO = userMapper.entityToDTO(it)

                UserSignInResDTO(
                        status = SignInResStatus.SIGN_IN_SUCCESS,
                        accessToken = jwtTokenProvider.createAccessToken(recentSignedInUserDTO),
                        user = recentSignedInUserDTO,
                ) to jwtTokenProvider.createRefreshToken(recentSignedInUserDTO.id)
            } ?: run {
                // account 는 존재하지만 user 가 존재하지 않는 경우
                UserSignInResDTO(
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

            UserSignInResDTO(
                    status = SignInResStatus.SIGN_UP_SUCCESS,
                    accountId = accountRepository.save(accountEntity).id ?: throw IllegalStateException("Registered Account ID cannot be null"),
            ) to null
        }
    }

    fun createUser(command: CreateUserCommand): Pair<CreateUserResDTO, String> {
        val account = accountRepository.findById(command.accountId)
                .orElseThrow { IllegalArgumentException("Account not found") }

        val createdUser = UserEntity(
                nickname = command.nickname,
                maidCafeId = command.maidCafeId,
        )

        account.addUser(createdUser)

        val userDTO = userMapper.entityToDTO(userRepository.save(createdUser))

        return CreateUserResDTO(
                user = userDTO,
                accessToken = jwtTokenProvider.createAccessToken(userDTO),
        ) to jwtTokenProvider.createRefreshToken(userDTO.id)
    }
}
