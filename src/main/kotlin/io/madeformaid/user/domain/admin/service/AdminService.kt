package io.madeformaid.user.domain.admin.service

import io.madeformaid.user.domain.admin.mapper.AdminMapper
import io.madeformaid.user.domain.user.repository.AccountRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class AdminService(
        private val accountRepository: AccountRepository,
        private val adminMapper: AdminMapper,
) {
}
