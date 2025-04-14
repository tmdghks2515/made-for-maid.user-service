package io.madeformaid.user.user.controller

import io.madeformaid.user.user.dto.command.*
import io.madeformaid.user.user.dto.data.SignInResDTO
import io.madeformaid.user.user.dto.data.UserDTO
import io.madeformaid.user.user.service.AdminService
import io.madeformaid.user.user.service.SignInService
import io.madeformaid.user.user.service.UserService
import io.madeformaid.user.utils.CookieProvider
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
        private val signInService: SignInService,
        private val userService: UserService,
        private val adminService: AdminService,
        private val cookieProvider: CookieProvider
) {
    @PostMapping("/signin/user/kakao")
    fun userKakaoSignIn(@RequestBody command: SignInCommand, response: HttpServletResponse): ResponseEntity<SignInResDTO> {
        val (signInResponse, refreshToken) = signInService.userkakaoSignIn(command)

        if (refreshToken != null) {
            response.setHeader(
                    HttpHeaders.SET_COOKIE,
                    cookieProvider.createRefreshTokenCookie(refreshToken).toString()
            )
        }

        return ResponseEntity.ok(signInResponse)
    }

    @PostMapping("/signin/admin/system/kakao")
    fun adminKakaoSignIn(@RequestBody command: SystemAdminSignInCommand, response: HttpServletResponse): ResponseEntity<SignInResDTO> {
        val (signInResponse, refreshToken) = signInService.systemAdminKakaoSignIn(command)

        refreshToken?.let {
            response.setHeader(
                    HttpHeaders.SET_COOKIE,
                    cookieProvider.createRefreshTokenCookie(it).toString()
            )
        }

        return ResponseEntity.ok(signInResponse)
    }

    @PostMapping("/signin/admin/maidcafe/kakao")
    fun adminKakaoSignIn(@RequestBody command: SignInCommand, response: HttpServletResponse): ResponseEntity<SignInResDTO> {
        val (signInResponse, refreshToken) = signInService.maidCafeAdminKakaoSignIn(command)

        refreshToken?.let {
            response.setHeader(
                    HttpHeaders.SET_COOKIE,
                    cookieProvider.createRefreshTokenCookie(it).toString()
            )
        }

        return ResponseEntity.ok(signInResponse)
    }


    @PostMapping("/user")
    fun createUser(@RequestBody command: CreateUserCommand, response: HttpServletResponse): ResponseEntity<String> {
        val (accessToken, refreshToken) = userService.createUser(command)

        response.setHeader(
                HttpHeaders.SET_COOKIE,
                cookieProvider.createRefreshTokenCookie(refreshToken).toString()
        )

        return ResponseEntity.ok(accessToken)
    }

    @PostMapping("/admin/system")
    fun createSystemAdmin(@RequestBody command: CreateSystemAdminCommand, response: HttpServletResponse): ResponseEntity<String> {
        val (accessToken, refreshToken) = adminService.createSystemAdmin(command)

        response.setHeader(
                HttpHeaders.SET_COOKIE,
                cookieProvider.createRefreshTokenCookie(refreshToken).toString()
        )

        return ResponseEntity.ok(accessToken)
    }

    @PostMapping("/admin/maidcafe")
    fun createMaidCafeAdmin(@RequestBody command: CreateMaidCafeAdminCommand, response: HttpServletResponse): ResponseEntity<String> {
        val (accessToken, refreshToken) = adminService.createMaidCafeAdmin(command)

        response.setHeader(
                HttpHeaders.SET_COOKIE,
                cookieProvider.createRefreshTokenCookie(refreshToken).toString()
        )

        return ResponseEntity.ok(accessToken)
    }
}
