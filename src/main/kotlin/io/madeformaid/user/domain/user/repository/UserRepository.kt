package io.madeformaid.user.domain.user.repository

import io.madeformaid.user.domain.user.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity, String> {
}
