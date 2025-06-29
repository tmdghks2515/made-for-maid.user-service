package io.madeformaid.user.domain.admin.mapper

import io.madeformaid.user.domain.admin.dto.data.AdminDTO
import io.madeformaid.user.domain.admin.dto.data.AdminProfileDTO
import io.madeformaid.user.domain.admin.dto.data.StaffDetailDTO
import io.madeformaid.user.domain.user.entity.UserEntity
import org.springframework.stereotype.Component

@Component
class AdminMapper {
    fun toAdminDTO(user: UserEntity): AdminDTO =
            AdminDTO(
                    id = user.id ?: error("Admin ID cannot be null"),
                    accountId = user.account?.id ?: error("Account ID cannot be null"),
                    nickname = user.nickname,
                    email = user.account?.email ?: error("Email cannot be null"),
                    shopId = user.shopId,
                    roles = user.roles,
                    profileImageId =  user.profileImageId,
                    profileImageUrl = user.profileImageUrl,
                    primaryRole = user.primaryRole,
                    staffType = user.staffType,
                    staffConcepts = user.staffConcepts,
                    approvedAt = user.approvedAt,
            )

    fun toAdminProfileDTO(user: UserEntity): AdminProfileDTO =
            AdminProfileDTO(
                    userId = user.id ?:error("User ID cannot be null"),
                    nickname = user.nickname,
                    profileImageUrl = user.profileImageUrl,
                    shopId = user.shopId ?: error("Shop ID cannot be null"),
                    staffType = user.staffType,
            )

        fun toStaffDetailDTO(user: UserEntity): StaffDetailDTO =
                StaffDetailDTO(
                        userId = user.id ?:error("User ID cannot be null"),
                        shopId = user.shopId ?: error("Shop ID cannot be null"),
                        nickname = user.nickname,
                        profileImageId = user.profileImageId,
                        profileImageUrl = user.profileImageUrl,
                        staffType = user.staffType ?: error("Staff Type cannot be null"),
                        staffConcepts = user.staffConcepts,
                        introduction = user.introduction,
                        approvedAt = user.approvedAt,
                )
}
