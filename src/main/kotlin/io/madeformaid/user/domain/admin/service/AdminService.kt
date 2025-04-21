package io.madeformaid.user.domain.admin.service

import io.madeformaid.user.domain.admin.dto.data.AdminProfileDTO
import io.madeformaid.user.domain.admin.mapper.AdminMapper
import io.madeformaid.user.domain.user.repository.AccountRepository
import io.madeformaid.user.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class AdminService(
        private val accountRepository: AccountRepository,
        private val userRepository: UserRepository,
        private val adminMapper: AdminMapper,
) {
    @Transactional(readOnly = true)
    fun getAdminProfiles(accountId: String): List<AdminProfileDTO> {
        return userRepository.findAdminsByAccountId(accountId)
                .map { adminMapper.toAdminProfileDTO(it) }
    }
}
