package io.madeformaid.user.domain.user.service

import io.madeformaid.user.domain.user.mapper.UserMapper
import io.madeformaid.user.domain.user.repository.AccountRepository
import io.madeformaid.user.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserService(
        private val userRepository: UserRepository,
        private val accountRepository: AccountRepository,
        private val userMapper: UserMapper,
) {
}
