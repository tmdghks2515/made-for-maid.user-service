package io.madeformaid.user.user.repository

import io.madeformaid.user.user.entity.AdminEntity
import org.springframework.data.jpa.repository.JpaRepository

interface AdminRepository : JpaRepository<AdminEntity, Long> {
}
