package io.madeformaid.user.domain.user.repository

import io.madeformaid.user.domain.user.entity.UserEntity
import io.madeformaid.user.domain.user.repository.custom.UserRepositoryCustom
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface UserRepository : JpaRepository<UserEntity, String>, UserRepositoryCustom {
    @Query(
        """
                SELECT u FROM UserEntity u
                JOIN u.roles r
                WHERE u.account.id = :accountId
                AND r IN (
                    io.madeformaid.shared.vo.enums.Role.SHOP_OWNER,
                    io.madeformaid.shared.vo.enums.Role.SHOP_MANAGER,
                    io.madeformaid.shared.vo.enums.Role.SHOP_STAFF,
                    io.madeformaid.shared.vo.enums.Role.SYSTEM_ADMIN,
                    io.madeformaid.shared.vo.enums.Role.SUPER_ADMIN
                )
            """
    )
    fun findAdminsByAccountId(accountId: String): List<UserEntity>
}
