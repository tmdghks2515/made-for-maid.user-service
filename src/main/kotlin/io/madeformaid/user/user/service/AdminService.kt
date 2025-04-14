package io.madeformaid.user.user.service

import io.madeformaid.shared.vo.enums.AdminRole
import io.madeformaid.user.user.dto.command.CreateMaidCafeAdminCommand
import io.madeformaid.user.user.dto.command.CreateSystemAdminCommand
import io.madeformaid.user.user.dto.data.MaidCafeAdminDTO
import io.madeformaid.user.user.dto.data.SystemAdminDTO
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
    fun createMaidCafeAdmin(command: CreateMaidCafeAdminCommand) : Pair<String, String> {
        val account = accountRepository.findById(command.accountId)
                .orElseThrow { IllegalArgumentException("Account with ID ${command.accountId} not found") }

        val createdAdmin = AdminEntity(
                account = account,
                nickname = command.nickname,
                maidCafeId = command.maidCafeId,
                adminRole = AdminRole.MAID_CAFE_ADMIN,
        )

        account.addMaidCafeAdmin(createdAdmin)
        val maidCafeAdminDTO = adminMapper.entityToMaidCafeAdminDTO(adminRepository.save(createdAdmin))

        return jwtTokenProvider.createAccessToken(maidCafeAdminDTO) to
                jwtTokenProvider.createRefreshToken(maidCafeAdminDTO.id)
    }

    fun createSystemAdmin(command: CreateSystemAdminCommand) : Pair<String, String> {
        val account = accountRepository.findById(command.accountId)
                .orElseThrow { IllegalArgumentException("Account with ID ${command.accountId} not found") }

        val createdAdmin = AdminEntity(
                account = account,
                nickname = command.nickname,
                adminRole = AdminRole.SYSTEM_ADMIN,
        )

        account.addSystemAdmin(createdAdmin)
        val systemAdminDTO = adminMapper.entityToSystemAdminDTO(adminRepository.save(createdAdmin))

        return jwtTokenProvider.createAccessToken(systemAdminDTO) to
                jwtTokenProvider.createRefreshToken(systemAdminDTO.id)
    }
}
