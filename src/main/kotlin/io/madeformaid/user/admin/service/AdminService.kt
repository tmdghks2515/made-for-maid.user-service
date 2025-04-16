package io.madeformaid.user.admin.service

import io.madeformaid.user.admin.mapper.AdminMapper
import io.madeformaid.user.user.repository.AccountRepository
import io.madeformaid.user.admin.repository.AdminRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class AdminService(
        private val adminRepository: AdminRepository,
        private val accountRepository: AccountRepository,
        private val adminMapper: AdminMapper,
) {
}
