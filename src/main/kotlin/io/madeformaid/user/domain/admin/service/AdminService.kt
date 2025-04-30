package io.madeformaid.user.domain.admin.service

import io.madeformaid.shared.vo.enums.Role
import io.madeformaid.user.domain.admin.dto.command.CreateAdminCommand
import io.madeformaid.user.domain.admin.dto.command.CreateStaffCommand
import io.madeformaid.user.domain.admin.dto.command.CreateSystemAdminCommand
import io.madeformaid.user.domain.admin.dto.data.AdminDTO
import io.madeformaid.webmvc.context.AuthContext
import io.madeformaid.webmvc.exception.BusinessException
import io.madeformaid.webmvc.exception.ErrorCode
import io.madeformaid.user.domain.admin.dto.data.AdminProfileDTO
import io.madeformaid.user.domain.admin.dto.data.AdminSignInResDTO
import io.madeformaid.user.domain.admin.mapper.AdminMapper
import io.madeformaid.user.domain.user.entity.UserEntity
import io.madeformaid.user.domain.user.repository.AccountRepository
import io.madeformaid.user.domain.user.repository.UserRepository
import io.madeformaid.user.grpc.client.ShopNameFetcher
import io.madeformaid.user.utils.JwtTokenProvider
import io.madeformaid.user.vo.SignInResStatus
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class AdminService(
        private val accountRepository: AccountRepository,
        private val userRepository: UserRepository,
        private val shopNameFetcher: ShopNameFetcher,
        private val adminMapper: AdminMapper,
        private val jwtTokenProvider: JwtTokenProvider,
        @Value("\${auth.system-secret}") private val systemSecret: String,
) {
    @Transactional(readOnly = true)
    fun getAdminProfiles(accountId: String): List<AdminProfileDTO> {
        val results = userRepository.findAdminsByAccountId(accountId)
            .map { adminMapper.toAdminProfileDTO(it) }

        runCatching {
            results.map { it.shopId }
                .distinct().takeIf { it.isNotEmpty() }?.let { shopIds ->
                    val shopNameMap = shopNameFetcher.fetchShopNames(shopIds)
                    results.forEach {
                        it.shopName = shopNameMap[it.shopId]
                    }
                }
        }

        return results
    }

    fun selectProfile(userId: String): Pair<AdminSignInResDTO, String> {
        val account = accountRepository.findById(AuthContext.getAccountId())
                .orElseThrow { BusinessException(ErrorCode.NOT_FOUND) }
        val admin = account.users.find { it.id == userId } ?: throw BusinessException(ErrorCode.NOT_FOUND)

        check(admin.isApproved()) { "승인되지 않은 프로필 입니다. 사장님에게 승인을 요청해주세요." }

        val adminDTO = adminMapper.toAdminDTO(admin)

        return AdminSignInResDTO(
                status = SignInResStatus.SIGN_IN_SUCCESS,
                accessToken = jwtTokenProvider.createAccessToken(adminDTO),
                admin = adminDTO,
        ) to jwtTokenProvider.createRefreshToken(adminDTO)
    }


    fun createShopOwner(command: CreateAdminCommand, accountId: String) : Pair<AdminSignInResDTO, String> {
        val account = accountRepository.findById(accountId)
            .orElseThrow { IllegalArgumentException("Account with ID $accountId not found") }

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

    fun createShopStaff(command: CreateStaffCommand, accountId: String) : String {
        val account = accountRepository.findById(accountId)
            .orElseThrow { IllegalArgumentException("Account with ID $accountId not found") }

        val createdStaff = UserEntity(
            account = account,
            nickname = command.nickname,
            shopId = command.shopId,
            roles = setOf(Role.USER, Role.SHOP_STAFF),
            staffType = command.staffType,
            staffConcepts = command.staffConcepts
        )

        account.addShopStaff(createdStaff)

        return userRepository.save(createdStaff).id ?: throw IllegalArgumentException("User ID cannot be null")
    }

    fun createShopManager(command: CreateAdminCommand, accountId: String) : String {
        val account = accountRepository.findById(accountId)
            .orElseThrow { IllegalArgumentException("Account with ID $accountId not found") }

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

    fun searchAdmins() : AdminDTO {
        TODO()
    }
}
