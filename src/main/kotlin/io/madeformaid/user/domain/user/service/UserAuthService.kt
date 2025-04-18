package io.madeformaid.user.domain.user.service

import io.madeformaid.shared.vo.enums.OauthProvider
import io.madeformaid.shared.vo.enums.Role
import io.madeformaid.shared.vo.enums.SignInResStatus
import io.madeformaid.user.domain.user.dto.command.CreateUserCommand
import io.madeformaid.user.domain.user.dto.command.UserKakaoSignInCommand
import io.madeformaid.user.domain.user.dto.data.CreateUserResDTO
import io.madeformaid.user.domain.user.dto.data.UserSignInResDTO
import io.madeformaid.user.domain.user.entity.AccountEntity
import io.madeformaid.user.domain.user.entity.UserEntity
import io.madeformaid.user.domain.user.mapper.UserMapper
import io.madeformaid.user.domain.user.repository.AccountRepository
import io.madeformaid.user.domain.user.repository.UserRepository
import io.madeformaid.user.utils.JwtTokenProvider
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserAuthService(
        private val accountRepository: AccountRepository,
        private val userRepository: UserRepository,
        private val userMapper: UserMapper,
        private val jwtTokenProvider: JwtTokenProvider,
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
                roles = setOf(Role.USER)
        )

        account.addUser(createdUser)
        val savedUser = userRepository.save(createdUser)
        account.recentUserId = savedUser.id

        val userDTO = userMapper.entityToDTO(savedUser)

        return CreateUserResDTO(
                user = userDTO,
                accessToken = jwtTokenProvider.createAccessToken(userDTO),
        ) to jwtTokenProvider.createRefreshToken(userDTO.id)
    }
}
