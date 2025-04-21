package io.madeformaid.user.domain.admin.mapper

import io.madeformaid.user.domain.admin.dto.data.AdminDTO
import io.madeformaid.user.domain.admin.dto.data.AdminProfileDTO
import io.madeformaid.user.domain.user.entity.UserEntity
import org.springframework.stereotype.Component

@Component
class AdminMapper {
    fun toAdminDTO(user: UserEntity): AdminDTO =
            AdminDTO(
                    id = user.id ?: throw IllegalArgumentException("Admin ID cannot be null"),
                    accountId = user.account?.id ?: throw IllegalArgumentException("Account ID cannot be null"),
                    nickname = user.nickname,
                    email = user.account?.email ?: throw IllegalArgumentException("Email cannot be null"),
                    cafeId = user.cafeId,
                    roles = user.roles,
                    profileImageUrl = user.profileImageUrl,
            )

    fun toAdminProfileDTO(user: UserEntity): AdminProfileDTO =
            AdminProfileDTO(
                    userId = user.id ?: throw IllegalArgumentException("User ID cannot be null"),
                    nickname = user.nickname,
                    profileImageUrl = user.profileImageUrl,
            )
}
