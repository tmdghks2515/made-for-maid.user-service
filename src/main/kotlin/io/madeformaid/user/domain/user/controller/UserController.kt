package io.madeformaid.user.domain.user.controller

import io.madeformaid.user.domain.user.dto.command.UpdateUserProfileCommand
import io.madeformaid.user.domain.user.dto.data.UserDTO
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user/user")
class UserController {
    fun updateProfile(command: UpdateUserProfileCommand): UserDTO {
        TODO()
    }
}
