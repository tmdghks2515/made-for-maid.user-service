package io.madeformaid.user.domain.user.mapper

import io.madeformaid.user.domain.user.dto.data.UserDTO
import io.madeformaid.user.domain.user.entity.UserEntity
import org.springframework.stereotype.Component

@Component
class UserMapper {
    fun entityToDTO(userEntity: UserEntity): UserDTO {
        return UserDTO(
                id = userEntity.id ?: throw IllegalArgumentException("User ID cannot be null"),
                accountId = userEntity.account?.id ?: throw IllegalArgumentException("Account ID cannot be null"),
                nickname = userEntity.nickname,
                maidCafeId = userEntity.maidCafeId ?: throw IllegalArgumentException("Maid Cafe ID cannot be null"),
                email = userEntity.account?.email ?: throw IllegalArgumentException("Email cannot be null"),
                profileImageUrl = userEntity.profileImageUrl,
                roles = userEntity.roles,
        )
    }
}
