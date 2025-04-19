package io.madeformaid.user.domain.admin.service

import io.madeformaid.user.vo.OauthProvider
import io.madeformaid.shared.vo.enums.Role
import io.madeformaid.user.domain.admin.dto.command.*
import io.madeformaid.user.domain.admin.dto.data.AdminSignInResDTO
import io.madeformaid.user.domain.user.entity.AccountEntity
import io.madeformaid.user.domain.admin.mapper.AdminMapper
import io.madeformaid.user.domain.user.entity.UserEntity
import io.madeformaid.user.domain.user.repository.AccountRepository
import io.madeformaid.user.domain.user.repository.UserRepository
import io.madeformaid.user.vo.SignInResStatus
import io.madeformaid.user.utils.JwtTokenProvider
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class AdminAuthService(
        private val accountRepository: AccountRepository,
        private val userRepository: UserRepository,
        private val adminMapper: AdminMapper,
        private val jwtTokenProvider: JwtTokenProvider,
        @Value("\${auth.system-secret}") private val systemSecret: String,
) {
    fun adminKakaoSignIn(command: AdminKakaoSignInCommand): Pair<AdminSignInResDTO, String?> {
        accountRepository.findByOauthIdAndOauthProvider(
                command.oauthId,
                OauthProvider.KAKAO
        )?.let { existingAccount ->
            existingAccount.getRecentSignedInAdmin()?.let { recentAdmin ->
                return AdminSignInResDTO(
                        status = SignInResStatus.SIGN_IN_SUCCESS,
                        accessToken = jwtTokenProvider.createAccessToken(
                                adminMapper.entityToAdminDTO(recentAdmin),
                        ),
                        admin = adminMapper.entityToAdminDTO(recentAdmin)
                ) to jwtTokenProvider.createRefreshToken(recentAdmin.id ?: throw IllegalArgumentException("Admin ID cannot be null"))
            }

            // admin 은 존재하지만 유효한 admin 이 존재하지 않는 경우 프로필 선택 화면으로 이동
            return AdminSignInResDTO(
                    status = SignInResStatus.PROFILE_SELECT,
                    accountId = existingAccount.id,
            ) to null
        } ?: run {
            val createdAccount = accountRepository.save(
                    AccountEntity(
                            email = command.email,
                            oauthProvider = OauthProvider.KAKAO,
                            oauthId = command.oauthId,
                    )
            )

            return AdminSignInResDTO(
                    status = SignInResStatus.SIGN_UP_SUCCESS,
                    accountId = createdAccount.id,
            ) to null
        }
    }

    fun createCafeOwner(command: CreateAdminCommand) : Pair<AdminSignInResDTO, String> {
        val account = accountRepository.findById(command.accountId)
                .orElseThrow { IllegalArgumentException("Account with ID ${command.accountId} not found") }

        val createdAdmin = UserEntity(
                account = account,
                nickname = command.nickname,
                roles = setOf(Role.USER, Role.CAFE_OWNER),
                cafeId = command.cafeId,
        )

        account.addCafeOwner(createdAdmin)
        val savedAdmin = userRepository.save(createdAdmin)
        account.recentAdminId = savedAdmin.id

        val cafeAdminDTO = adminMapper.entityToAdminDTO(savedAdmin)

        return AdminSignInResDTO(
                status = SignInResStatus.SIGN_IN_SUCCESS,
                admin = cafeAdminDTO,
                accessToken = jwtTokenProvider.createAccessToken(cafeAdminDTO),
        ) to jwtTokenProvider.createRefreshToken(cafeAdminDTO.id)
    }

    fun createCafeStaff(command: CreateAdminCommand) : String {
        val account = accountRepository.findById(command.accountId)
                .orElseThrow { IllegalArgumentException("Account with ID ${command.accountId} not found") }

        val createdStaff = UserEntity(
                account = account,
                nickname = command.nickname,
                cafeId = command.cafeId,
                roles = setOf(Role.USER, Role.CAFE_STAFF),
        )

        account.addCafeStaff(createdStaff)

        return userRepository.save(createdStaff).id ?: throw IllegalArgumentException("User ID cannot be null")
    }

    fun createCafeManager(command: CreateAdminCommand) : String {
        val account = accountRepository.findById(command.accountId)
                .orElseThrow { IllegalArgumentException("Account with ID ${command.accountId} not found") }

        val createdManager = UserEntity(
                account = account,
                nickname = command.nickname,
                cafeId = command.cafeId,
                roles = setOf(Role.USER, Role.CAFE_MANAGER),
        )

        account.addCafeManager(createdManager)

        return userRepository.save(createdManager).id ?: throw IllegalArgumentException("User ID cannot be null")
    }

    fun createSystemAdmin(command: CreateSystemAdminCommand) : Pair<AdminSignInResDTO, String> {
        require(command.systemSecret == systemSecret) { "Invalid system secret" }

        val account = accountRepository.findById(command.accountId)
                .orElseThrow { IllegalArgumentException("Account with ID ${command.accountId} not found") }

        val createdAdmin = UserEntity(
                account = account,
                nickname = command.nickname,
                roles = setOf(Role.USER, Role.SYSTEM_ADMIN),
        )

        account.addSystemAdmin(createdAdmin)
        val savedAdmin = userRepository.save(createdAdmin)
        account.recentAdminId = savedAdmin.id

        val systemAdminDTO = adminMapper.entityToAdminDTO(savedAdmin)

        return AdminSignInResDTO(
                status = SignInResStatus.SIGN_IN_SUCCESS,
                admin = systemAdminDTO,
                accessToken = jwtTokenProvider.createAccessToken(systemAdminDTO),
        ) to jwtTokenProvider.createRefreshToken(systemAdminDTO.id)
    }
}
