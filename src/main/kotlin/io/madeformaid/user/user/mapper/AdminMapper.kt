package io.madeformaid.user.user.mapper

import io.madeformaid.user.admin.dto.data.AdminDTO
import io.madeformaid.user.user.entity.AdminEntity
import org.springframework.stereotype.Component

@Component
class AdminMapper {
    fun entityToAdminDTO(adminEntity: AdminEntity): AdminDTO =
            AdminDTO(
                    id = adminEntity.id ?: throw IllegalArgumentException("Admin ID cannot be null"),
                    accountId = adminEntity.account?.id ?: throw IllegalArgumentException("Account ID cannot be null"),
                    nickname = adminEntity.nickname,
                    email = adminEntity.account?.email ?: throw IllegalArgumentException("Email cannot be null"),
                    maidCafeId = adminEntity.maidCafeId,
                    adminRole = adminEntity.adminRole,
                    profileImageUrl = adminEntity.profileImageUrl,
            )
}
