package io.madeformaid.user.admin.controller

import io.madeformaid.user.admin.dto.command.AdminKakaoSignInCommand
import io.madeformaid.user.admin.service.AdminAuthService
import io.madeformaid.user.admin.dto.command.CreateMaidCafeAdminCommand
import io.madeformaid.user.admin.dto.command.CreateMaidCommand
import io.madeformaid.user.admin.dto.command.CreateSystemAdminCommand
import io.madeformaid.user.admin.dto.data.AdminSignInResDTO
import io.madeformaid.user.admin.dto.data.CreateAdminResDTO
import io.madeformaid.user.admin.service.AdminService
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
        private val adminService: AdminService,
        private val cookieProvider: CookieProvider
) {
    @PostMapping("/signin/admin/kakao")
    fun adminKakaoSignIn(@RequestBody command: AdminKakaoSignInCommand, response: HttpServletResponse): ResponseEntity<AdminSignInResDTO> {
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
    fun createSystemAdmin(@RequestBody command: CreateSystemAdminCommand, response: HttpServletResponse): ResponseEntity<CreateAdminResDTO> {
        val (accessToken, refreshToken) = adminService.createSystemAdmin(command)

        response.setHeader(
                HttpHeaders.SET_COOKIE,
                cookieProvider.createRefreshTokenCookie(refreshToken).toString()
        )

        return ResponseEntity.ok(accessToken)
    }

    @PostMapping("/maidcafe")
    fun createMaidCafeAdmin(@RequestBody command: CreateMaidCafeAdminCommand, response: HttpServletResponse): ResponseEntity<CreateAdminResDTO> {
        val (accessToken, refreshToken) = adminService.createMaidCafeAdmin(command)

        response.setHeader(
                HttpHeaders.SET_COOKIE,
                cookieProvider.createRefreshTokenCookie(refreshToken).toString()
        )

        return ResponseEntity.ok(accessToken)
    }

    @PostMapping("/maid")
    fun createMaid(@RequestBody command: CreateMaidCommand, response: HttpServletResponse): ResponseEntity<CreateAdminResDTO> {
        val (accessToken, refreshToken) = adminService.createMaid(command)

        response.setHeader(
                HttpHeaders.SET_COOKIE,
                cookieProvider.createRefreshTokenCookie(refreshToken).toString()
        )

        return ResponseEntity.ok(accessToken)
    }
}
