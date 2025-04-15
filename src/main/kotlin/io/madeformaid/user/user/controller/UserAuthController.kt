package io.madeformaid.user.user.controller

import io.madeformaid.user.user.dto.command.*
import io.madeformaid.user.user.dto.data.CreateUserResDTO
import io.madeformaid.user.user.dto.data.UserSignInResDTO
import io.madeformaid.user.user.service.UserAuthService
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
@RequestMapping("/api/user/auth/user")
class UserAuthController(
        private val userAuthService: UserAuthService,
        private val userService: UserService,
        private val cookieProvider: CookieProvider
) {
    @PostMapping("/signin/kakao")
    fun userKakaoSignIn(@RequestBody command: UserKakaoSignInCommand, response: HttpServletResponse): ResponseEntity<UserSignInResDTO> {
        val (signInResponse, refreshToken) = userAuthService.kakaoSignIn(command)

        if (refreshToken != null) {
            response.setHeader(
                    HttpHeaders.SET_COOKIE,
                    cookieProvider.createRefreshTokenCookie(refreshToken).toString()
            )
        }

        return ResponseEntity.ok(signInResponse)
    }

    @PostMapping("/user")
    fun createUser(@RequestBody command: CreateUserCommand, response: HttpServletResponse): ResponseEntity<CreateUserResDTO> {
        val (responseDTO, refreshToken) = userService.createUser(command)

        response.setHeader(
                HttpHeaders.SET_COOKIE,
                cookieProvider.createRefreshTokenCookie(refreshToken).toString()
        )

        return ResponseEntity.ok(responseDTO)
    }

}
