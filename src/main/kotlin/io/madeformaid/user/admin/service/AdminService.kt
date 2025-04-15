package io.madeformaid.user.admin.service

import io.madeformaid.shared.vo.enums.AdminRole
import io.madeformaid.user.admin.dto.command.CreateMaidCafeAdminCommand
import io.madeformaid.user.admin.dto.command.CreateMaidCommand
import io.madeformaid.user.admin.dto.command.CreateSystemAdminCommand
import io.madeformaid.user.admin.dto.data.CreateAdminResDTO
import io.madeformaid.user.user.entity.AdminEntity
import io.madeformaid.user.user.mapper.AdminMapper
import io.madeformaid.user.user.repository.AccountRepository
import io.madeformaid.user.user.repository.AdminRepository
import io.madeformaid.user.utils.JwtTokenProvider
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class AdminService(
        private val adminRepository: AdminRepository,
        private val accountRepository: AccountRepository,
        private val adminMapper: AdminMapper,
        private val jwtTokenProvider: JwtTokenProvider
) {
    fun createMaidCafeAdmin(command: CreateMaidCafeAdminCommand) : Pair<CreateAdminResDTO, String> {
        val account = accountRepository.findById(command.accountId)
                .orElseThrow { IllegalArgumentException("Account with ID ${command.accountId} not found") }

        val createdAdmin = AdminEntity(
                account = account,
                nickname = command.nickname,
                maidCafeId = command.maidCafeId,
                adminRole = AdminRole.MAID_CAFE_ADMIN,
        )

        account.addMaidCafeAdmin(createdAdmin)
        val maidCafeAdminDTO = adminMapper.entityToAdminDTO(adminRepository.save(createdAdmin))

        return CreateAdminResDTO(
                admin = maidCafeAdminDTO,
                accessToken = jwtTokenProvider.createAccessToken(maidCafeAdminDTO),
        ) to jwtTokenProvider.createRefreshToken(maidCafeAdminDTO.id)
    }

    fun createSystemAdmin(command: CreateSystemAdminCommand) : Pair<CreateAdminResDTO, String> {
        val account = accountRepository.findById(command.accountId)
                .orElseThrow { IllegalArgumentException("Account with ID ${command.accountId} not found") }

        val createdAdmin = AdminEntity(
                account = account,
                nickname = command.nickname,
                adminRole = AdminRole.SYSTEM_ADMIN,
        )

        account.addSystemAdmin(createdAdmin)
        val systemAdminDTO = adminMapper.entityToAdminDTO(adminRepository.save(createdAdmin))

        return CreateAdminResDTO(
                admin = systemAdminDTO,
                accessToken = jwtTokenProvider.createAccessToken(systemAdminDTO),
        ) to jwtTokenProvider.createRefreshToken(systemAdminDTO.id)
    }

    fun createMaid(command: CreateMaidCommand) : Pair<CreateAdminResDTO, String> {
        val account = accountRepository.findById(command.accountId)
                .orElseThrow { IllegalArgumentException("Account with ID ${command.accountId} not found") }

        val createdAdmin = AdminEntity(
                account = account,
                nickname = command.nickname,
                maidCafeId = command.maidCafeId,
                adminRole = AdminRole.MAID,
        )

        account.addMaid(createdAdmin)
        val maidDTO = adminMapper.entityToAdminDTO(adminRepository.save(createdAdmin))

        return CreateAdminResDTO(
                admin = maidDTO,
                accessToken = jwtTokenProvider.createAccessToken(maidDTO),
        ) to jwtTokenProvider.createRefreshToken(maidDTO.id)
    }
}
