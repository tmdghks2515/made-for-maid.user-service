package io.madeformaid.user.admin.service

import io.madeformaid.shared.vo.enums.AdminRole
import io.madeformaid.shared.vo.enums.OauthProvider
import io.madeformaid.shared.vo.enums.SignInResStatus
import io.madeformaid.user.admin.dto.command.*
import io.madeformaid.user.admin.dto.data.AdminSignInResDTO
import io.madeformaid.user.admin.entity.AdminEntity
import io.madeformaid.user.user.entity.AccountEntity
import io.madeformaid.user.admin.mapper.AdminMapper
import io.madeformaid.user.admin.repository.AdminRepository
import io.madeformaid.user.user.repository.AccountRepository
import io.madeformaid.user.utils.JwtTokenProvider
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class AdminAuthService(
        private val accountRepository: AccountRepository,
        private val adminRepository: AdminRepository,
        private val adminMapper: AdminMapper,
        private val jwtTokenProvider: JwtTokenProvider,
        @Value("\${system.secret}") private val systemSecret: String,
) {
    fun adminKakaoSignIn(command: AdminKakaoSignInCommand): Pair<AdminSignInResDTO, String?> {
        accountRepository.findByOauthIdAndOauthProvider(
                command.oauthId,
                OauthProvider.KAKAO
        )?.let { existingAccount ->
            if (existingAccount.admins.isEmpty()) {
                return AdminSignInResDTO(
                        status = SignInResStatus.SIGN_UP_SUCCESS,
                        accountId = existingAccount.id,
                ) to null
            }

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

    fun createMaidCafeOwner(command: CreateAdminCommand) : Pair<AdminSignInResDTO, String> {
        val account = accountRepository.findById(command.accountId)
                .orElseThrow { IllegalArgumentException("Account with ID ${command.accountId} not found") }

        val createdAdmin = AdminEntity(
                account = account,
                nickname = command.nickname,
                adminRole = AdminRole.MAID_CAFE_OWNER,
                maidCafeId = command.maidCafeId,
        )

        account.addMaidCafeOwner(createdAdmin)
        val savedAdmin = adminRepository.save(createdAdmin)
        account.recentAdminId = savedAdmin.id

        val maidCafeAdminDTO = adminMapper.entityToAdminDTO(savedAdmin)

        return AdminSignInResDTO(
                status = SignInResStatus.SIGN_IN_SUCCESS,
                admin = maidCafeAdminDTO,
                accessToken = jwtTokenProvider.createAccessToken(maidCafeAdminDTO),
        ) to jwtTokenProvider.createRefreshToken(maidCafeAdminDTO.id)
    }

    fun createSystemAdmin(command: CreateSystemAdminCommand) : Pair<AdminSignInResDTO, String> {
        require(command.systemSecret == systemSecret) { "Invalid system secret" }

        val account = accountRepository.findById(command.accountId)
                .orElseThrow { IllegalArgumentException("Account with ID ${command.accountId} not found") }

        val createdAdmin = AdminEntity(
                account = account,
                nickname = command.nickname,
                adminRole = AdminRole.SYSTEM_ADMIN,
        )

        account.addSystemAdmin(createdAdmin)
        val savedAdmin = adminRepository.save(createdAdmin)
        account.recentAdminId = savedAdmin.id

        val systemAdminDTO = adminMapper.entityToAdminDTO(savedAdmin)

        return AdminSignInResDTO(
                status = SignInResStatus.SIGN_IN_SUCCESS,
                admin = systemAdminDTO,
                accessToken = jwtTokenProvider.createAccessToken(systemAdminDTO),
        ) to jwtTokenProvider.createRefreshToken(systemAdminDTO.id)
    }

    fun createMaid(command: CreateAdminCommand) : String {
        val account = accountRepository.findById(command.accountId)
                .orElseThrow { IllegalArgumentException("Account with ID ${command.accountId} not found") }

        val createdMaid = AdminEntity(
                account = account,
                nickname = command.nickname,
                maidCafeId = command.maidCafeId,
                adminRole = AdminRole.MAID,
        )

        account.addMaid(createdMaid)

        return adminRepository.save(createdMaid).id ?: throw IllegalArgumentException("Admin ID cannot be null")
    }

    fun createMaidCafeManager(command: CreateAdminCommand) : String {
        val account = accountRepository.findById(command.accountId)
                .orElseThrow { IllegalArgumentException("Account with ID ${command.accountId} not found") }

        val createdManager = AdminEntity(
                account = account,
                nickname = command.nickname,
                maidCafeId = command.maidCafeId,
                adminRole = AdminRole.MAID_CAFE_MANAGER,
        )

        account.addMaidCafeManager(createdManager)

        return adminRepository.save(createdManager).id ?: throw IllegalArgumentException("Admin ID cannot be null")
    }
}
