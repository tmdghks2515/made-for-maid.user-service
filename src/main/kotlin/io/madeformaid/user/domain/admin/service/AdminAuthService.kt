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

    fun createShopOwner(command: CreateAdminCommand) : Pair<AdminSignInResDTO, String> {
        val account = accountRepository.findById(command.accountId)
                .orElseThrow { IllegalArgumentException("Account with ID ${command.accountId} not found") }

        val createdAdmin = UserEntity(
                account = account,
                nickname = command.nickname,
                roles = setOf(Role.USER, Role.SHOP_OWNER),
                shopId = command.shopId,
        )

        account.addShopOwner(createdAdmin)
        val savedAdmin = userRepository.save(createdAdmin)
        account.recentAdminId = savedAdmin.id

        val shopAdminDTO = adminMapper.toAdminDTO(savedAdmin)

        return AdminSignInResDTO(
                status = SignInResStatus.SIGN_IN_SUCCESS,
                admin = shopAdminDTO,
                accessToken = jwtTokenProvider.createAccessToken(shopAdminDTO),
        ) to jwtTokenProvider.createRefreshToken(shopAdminDTO)
    }

    fun createShopStaff(command: CreateAdminCommand) : String {
        val account = accountRepository.findById(command.accountId)
                .orElseThrow { IllegalArgumentException("Account with ID ${command.accountId} not found") }

        val createdStaff = UserEntity(
                account = account,
                nickname = command.nickname,
                shopId = command.shopId,
                roles = setOf(Role.USER, Role.SHOP_STAFF),
        )

        account.addShopStaff(createdStaff)

        return userRepository.save(createdStaff).id ?: throw IllegalArgumentException("User ID cannot be null")
    }

    fun createShopManager(command: CreateAdminCommand) : String {
        val account = accountRepository.findById(command.accountId)
                .orElseThrow { IllegalArgumentException("Account with ID ${command.accountId} not found") }

        val createdManager = UserEntity(
                account = account,
                nickname = command.nickname,
                shopId = command.shopId,
                roles = setOf(Role.USER, Role.SHOP_MANAGER),
        )

        account.addShopManager(createdManager)

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

        val systemAdminDTO = adminMapper.toAdminDTO(savedAdmin)

        return AdminSignInResDTO(
                status = SignInResStatus.SIGN_IN_SUCCESS,
                admin = systemAdminDTO,
                accessToken = jwtTokenProvider.createAccessToken(systemAdminDTO),
        ) to jwtTokenProvider.createRefreshToken(systemAdminDTO)
    }
}
