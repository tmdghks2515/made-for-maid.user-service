package io.made_for_maid.user_service.user.mapper

import io.made_for_maid.user_service.user.dto.data.UserDTO
import io.made_for_maid.user_service.user.entity.UserEntity
import org.springframework.stereotype.Component

@Component
class UserMapper {
    fun entityToDTO(userEntity: UserEntity): UserDTO {
        return UserDTO(
                id = userEntity.id ?: throw IllegalArgumentException("User ID cannot be null"),
                accountId = userEntity.account.id ?: throw IllegalArgumentException("Account ID cannot be null"),
                nickname = userEntity.nickname,
                currentMaidCafeId = userEntity.maidCafeId,
                maidCafeIds = userEntity.account.users.map { it.maidCafeId },
                email = userEntity.account.email,
                joinedAt = userEntity.createdAt,
                accountJoinedAt = userEntity.account.createdAt,
        )
    }
}
