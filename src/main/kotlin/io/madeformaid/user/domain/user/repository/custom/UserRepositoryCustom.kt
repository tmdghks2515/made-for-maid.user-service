package io.madeformaid.user.domain.user.repository.custom

import io.madeformaid.user.domain.admin.dto.query.SearchAdminQuery
import io.madeformaid.user.domain.user.entity.UserEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface UserRepositoryCustom {
    fun searchAdmins(
        query: SearchAdminQuery,
        pageable: Pageable
    ): Page<UserEntity>
}
