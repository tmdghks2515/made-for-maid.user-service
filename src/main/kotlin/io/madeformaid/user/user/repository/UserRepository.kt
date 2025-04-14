package io.madeformaid.user.user.repository

import io.madeformaid.user.user.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity, String> {
}
