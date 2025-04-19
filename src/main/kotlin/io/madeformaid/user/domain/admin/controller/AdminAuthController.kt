package io.madeformaid.user.domain.admin.controller

import io.madeformaid.user.domain.admin.dto.command.*
import io.madeformaid.user.domain.admin.dto.data.AdminSignInResDTO
import io.madeformaid.user.domain.admin.service.AdminAuthService
import io.madeformaid.user.utils.CookieProvider
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user/auth/admin")
class AdminAuthController(
        private val adminAuthService: AdminAuthService,
        private val cookieProvider: CookieProvider
) {
    @PostMapping("/signin/kakao")
    fun adminKakaoSignIn(
            @RequestBody command: AdminKakaoSignInCommand,
            response: HttpServletResponse
    ): ResponseEntity<AdminSignInResDTO> {
        val (signInResponse, refreshToken) = adminAuthService.adminKakaoSignIn(command)

        refreshToken?.let {
            response.setHeader(
                    HttpHeaders.SET_COOKIE,
                    cookieProvider.createRefreshTokenCookie(it).toString()
            )
        }

        return ResponseEntity.ok(signInResponse)
    }

    @PostMapping("/system")
    fun createSystemAdmin(
            @RequestBody command: CreateSystemAdminCommand,
            response: HttpServletResponse
    ): ResponseEntity<AdminSignInResDTO> {
        val (signInResponse, refreshToken) = adminAuthService.createSystemAdmin(command)

        response.setHeader(
                HttpHeaders.SET_COOKIE,
                cookieProvider.createRefreshTokenCookie(refreshToken).toString()
        )

        return ResponseEntity.ok(signInResponse)
    }

    @PostMapping("/owner")
    fun createCafeOwner(
            @RequestBody command: CreateAdminCommand,
            response: HttpServletResponse
    ): ResponseEntity<AdminSignInResDTO> {
        val (signInResponse, refreshToken) = adminAuthService.createCafeOwner(command)

        response.setHeader(
                HttpHeaders.SET_COOKIE,
                cookieProvider.createRefreshTokenCookie(refreshToken).toString()
        )

        return ResponseEntity.ok(signInResponse)
    }

    @PostMapping("/manager")
    fun createCafeManager(
            @RequestBody command: CreateAdminCommand,
    ): ResponseEntity<String> =
            ResponseEntity.ok(adminAuthService.createCafeManager(command))

    @PostMapping("/staff")
    fun createCafeStaff(
            @RequestBody command: CreateAdminCommand,
    ): ResponseEntity<String> =
            ResponseEntity.ok(adminAuthService.createCafeStaff(command))
}
