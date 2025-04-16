package io.madeformaid.user.admin.repository

import io.madeformaid.user.admin.entity.AdminEntity
import org.springframework.data.jpa.repository.JpaRepository

interface AdminRepository : JpaRepository<AdminEntity, Long> {
}
