package io.made_for_maid.user_service.user.controller

import io.made_for_maid.user_service.user.dto.command.CreateUserCommand
import io.made_for_maid.user_service.user.dto.command.SignUpCommand
import io.made_for_maid.user_service.user.dto.data.UserDTO
import io.made_for_maid.user_service.user.service.AccountService
import io.made_for_maid.user_service.user.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
        private val accountService: AccountService,
        private val userService: UserService,
) {
    @PostMapping("/signup/kakao")
    fun signUpByKakao(@RequestBody command: SignUpCommand): ResponseEntity<String> =
            ResponseEntity.ok(
                    accountService.signUpByKakao(command)
            )

    @PostMapping("/user")
    fun createUser(@RequestBody command: CreateUserCommand): ResponseEntity<UserDTO> =
            ResponseEntity.ok(
                    userService.createUser(command)
            )
}
