package io.madeformaid.user.user.controller

import io.madeformaid.user.user.dto.command.UpdateUserProfileCommand
import io.madeformaid.user.user.dto.data.UserDTO
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user/user")
class UserController {
    fun updateProfile(command: UpdateUserProfileCommand): UserDTO {
        TODO()
    }
}
