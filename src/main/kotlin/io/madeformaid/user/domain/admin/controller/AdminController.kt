package io.madeformaid.user.domain.admin.controller

import io.madeformaid.webmvc.context.AuthContext
import io.madeformaid.user.domain.admin.dto.data.AdminProfileDTO
import io.madeformaid.user.domain.admin.dto.data.AdminSignInResDTO
import io.madeformaid.user.domain.admin.service.AdminService
import io.madeformaid.user.utils.CookieProvider
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user/admin")
class AdminController(
        private val adminService: AdminService,
        private val cookieProvider: CookieProvider
) {
    @GetMapping("/profiles")
    fun getProfiles(): ResponseEntity<List<AdminProfileDTO>> {
        return ResponseEntity.ok(adminService.getAdminProfiles(AuthContext.getAccountId()))
    }

    @PostMapping("/profile/{userId}")
    fun selectProfile(
            @PathVariable userId: String,
            response: HttpServletResponse
    ): ResponseEntity<AdminSignInResDTO> {
        val (signInResponse, refreshToken) = adminService.selectProfile(userId)

        response.setHeader(
                HttpHeaders.SET_COOKIE,
                cookieProvider.createRefreshTokenCookie(refreshToken).toString()
        )

        return ResponseEntity.ok(signInResponse)
    }
}
