package io.madeformaid.user.domain.admin.mapper

import io.madeformaid.user.domain.admin.dto.data.AdminDTO
import io.madeformaid.user.domain.user.entity.UserEntity
import org.springframework.stereotype.Component

@Component
class AdminMapper {
    fun entityToAdminDTO(user: UserEntity): AdminDTO =
            AdminDTO(
                    id = user.id ?: throw IllegalArgumentException("Admin ID cannot be null"),
                    accountId = user.account?.id ?: throw IllegalArgumentException("Account ID cannot be null"),
                    nickname = user.nickname,
                    email = user.account?.email ?: throw IllegalArgumentException("Email cannot be null"),
                    maidCafeId = user.maidCafeId,
                    roles = user.roles,
                    profileImageUrl = user.profileImageUrl,
            )
}
