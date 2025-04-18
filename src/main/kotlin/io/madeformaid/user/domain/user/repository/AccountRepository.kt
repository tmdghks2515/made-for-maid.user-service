package io.madeformaid.user.domain.user.repository

import io.madeformaid.shared.vo.enums.OauthProvider
import io.madeformaid.user.domain.user.entity.AccountEntity
import org.springframework.data.jpa.repository.JpaRepository

interface AccountRepository : JpaRepository<AccountEntity, String> {
    fun findByOauthIdAndOauthProvider(
        oauthId: String,
        oauthProvider: OauthProvider,
    ): AccountEntity?
}
