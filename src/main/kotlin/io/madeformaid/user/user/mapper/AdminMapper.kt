package io.madeformaid.user.user.mapper

import io.madeformaid.user.user.dto.data.MaidCafeAdminDTO
import io.madeformaid.user.user.dto.data.SystemAdminDTO
import io.madeformaid.user.user.entity.AdminEntity
import org.springframework.stereotype.Component

@Component
class AdminMapper {
    fun entityToMaidCafeAdminDTO(adminEntity: AdminEntity): MaidCafeAdminDTO =
            MaidCafeAdminDTO(
                    id = adminEntity.id ?: throw IllegalArgumentException("Admin ID cannot be null"),
                    accountId = adminEntity.account?.id ?: throw IllegalArgumentException("Account ID cannot be null"),
                    nickname = adminEntity.nickname,
                    email = adminEntity.account?.email ?: throw IllegalArgumentException("Email cannot be null"),
                    currentMaidCafeId = adminEntity.maidCafeId ?: throw IllegalArgumentException("Maid Cafe ID cannot be null"),
            )

    fun entityToSystemAdminDTO(adminEntity: AdminEntity): SystemAdminDTO =
            SystemAdminDTO(
                    id = adminEntity.id ?: throw IllegalArgumentException("Admin ID cannot be null"),
                    accountId = adminEntity.account?.id ?: throw IllegalArgumentException("Account ID cannot be null"),
                    nickname = adminEntity.nickname,
                    email = adminEntity.account?.email ?: throw IllegalArgumentException("Email cannot be null"),
            )
}
