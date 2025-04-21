package io.madeformaid.user.domain.admin.controller

import io.madeformaid.shared.context.AuthContext
import io.madeformaid.user.domain.admin.service.AdminService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user/admin")
class AdminController(
        private val adminService: AdminService,
) {
    @GetMapping("/profiles")
    fun getProfiles(): ResponseEntity<Any> {
        return ResponseEntity.ok(adminService.getAdminProfiles(AuthContext.getAccountId()))
    }
}
