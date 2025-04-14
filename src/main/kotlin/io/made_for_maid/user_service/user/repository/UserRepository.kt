package io.made_for_maid.user_service.user.repository

import io.made_for_maid.user_service.user.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity, String> {
}
