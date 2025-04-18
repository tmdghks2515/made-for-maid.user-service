package io.madeformaid.user.domain.user.controller

import io.madeformaid.user.domain.user.dto.command.*
import io.madeformaid.user.domain.user.dto.data.CreateUserResDTO
import io.madeformaid.user.domain.user.dto.data.UserSignInResDTO
import io.madeformaid.user.domain.user.service.UserAuthService
import io.madeformaid.user.domain.user.service.UserService
import io.madeformaid.user.utils.CookieProvider
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user/auth")
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

    @PostMapping
    fun createUser(@RequestBody command: CreateUserCommand, response: HttpServletResponse): ResponseEntity<CreateUserResDTO> {
        val (responseDTO, refreshToken) = userAuthService.createUser(command)

        response.setHeader(
                HttpHeaders.SET_COOKIE,
                cookieProvider.createRefreshTokenCookie(refreshToken).toString()
        )

        return ResponseEntity.ok(responseDTO)
    }

}
