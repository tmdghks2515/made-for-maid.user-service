package io.made_for_maid.user_service.user.repository

import io.made_for_maid.user_service.user.entity.AccountEntity
import org.springframework.data.jpa.repository.JpaRepository

interface AccountRepository : JpaRepository<AccountEntity, String> {
}
