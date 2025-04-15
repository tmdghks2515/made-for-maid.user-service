package io.madeformaid.user.user.service

import io.madeformaid.user.user.dto.command.CreateUserCommand
import io.madeformaid.user.user.dto.data.CreateUserResDTO
import io.madeformaid.user.user.dto.data.UserDTO
import io.madeformaid.user.user.entity.UserEntity
import io.madeformaid.user.user.mapper.UserMapper
import io.madeformaid.user.user.repository.AccountRepository
import io.madeformaid.user.user.repository.UserRepository
import io.madeformaid.user.utils.JwtTokenProvider
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserService(
        private val userRepository: UserRepository,
        private val accountRepository: AccountRepository,
        private val userMapper: UserMapper,
        private val jwtTokenProvider: JwtTokenProvider
) {
    fun createUser(command: CreateUserCommand): Pair<CreateUserResDTO, String> {
        val account = accountRepository.findById(command.accountId)
                .orElseThrow { IllegalArgumentException("Account not found") }

        val createdUser = UserEntity(
                nickname = command.nickname,
                maidCafeId = command.maidCafeId,
        )

        account.addUser(createdUser)

        val userDTO = userMapper.entityToDTO(userRepository.save(createdUser))

        return CreateUserResDTO(
                user = userDTO,
                accessToken = jwtTokenProvider.createAccessToken(userDTO),
        ) to jwtTokenProvider.createRefreshToken(userDTO.id)
    }
}
