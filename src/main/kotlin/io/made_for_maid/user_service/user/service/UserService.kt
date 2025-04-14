package io.made_for_maid.user_service.user.service

import io.made_for_maid.user_service.user.dto.command.CreateUserCommand
import io.made_for_maid.user_service.user.dto.data.UserDTO
import io.made_for_maid.user_service.user.entity.UserEntity
import io.made_for_maid.user_service.user.mapper.UserMapper
import io.made_for_maid.user_service.user.repository.AccountRepository
import io.made_for_maid.user_service.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserService(
        private val userRepository: UserRepository,
        private val accountRepository: AccountRepository,
        private val userMapper: UserMapper
) {
    fun createUser(command: CreateUserCommand): UserDTO {
        val account = accountRepository.findById(command.accountId)
                .orElseThrow { IllegalArgumentException("Account not found") }

        val userEntity = UserEntity(
                nickname = command.nickname,
                maidCafeId = command.maidCafeId,
                account = account,
        )

        account.users.add(userEntity)

        return userMapper.entityToDTO(userRepository.save(userEntity))
    }
}
