package io.madeformaid.user.user.mapper

import io.madeformaid.user.user.dto.data.UserDTO
import io.madeformaid.user.user.entity.UserEntity
import org.springframework.stereotype.Component

@Component
class UserMapper {
    fun entityToDTO(userEntity: UserEntity): UserDTO {
        return UserDTO(
                id = userEntity.id ?: throw IllegalArgumentException("User ID cannot be null"),
                accountId = userEntity.account?.id ?: throw IllegalArgumentException("Account ID cannot be null"),
                nickname = userEntity.nickname,
                currentMaidCafeId = userEntity.maidCafeId,
                email = userEntity.account?.email ?: throw IllegalArgumentException("Email cannot be null"),
        )
    }
}
