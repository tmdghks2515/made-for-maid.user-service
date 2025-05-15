package io.madeformaid.user.domain.admin.controller

import io.madeformaid.user.domain.admin.dto.command.CreateAdminCommand
import io.madeformaid.user.domain.admin.dto.command.CreateStaffCommand
import io.madeformaid.user.domain.admin.dto.command.CreateSystemAdminCommand
import io.madeformaid.user.domain.admin.dto.command.UpdateProfileCommand
import io.madeformaid.user.domain.admin.dto.command.UpdateStaffConceptsCommand
import io.madeformaid.user.domain.admin.dto.command.UpdateStaffIntroductionCommand
import io.madeformaid.user.domain.admin.dto.data.AdminDTO
import io.madeformaid.webmvc.context.AuthContext
import io.madeformaid.user.domain.admin.dto.data.AdminProfileDTO
import io.madeformaid.user.domain.admin.dto.data.AdminSignInResDTO
import io.madeformaid.user.domain.admin.dto.data.StaffDetailDTO
import io.madeformaid.user.domain.admin.dto.query.SearchAdminQuery
import io.madeformaid.user.domain.admin.service.AdminQueryService
import io.madeformaid.user.domain.admin.service.AdminService
import io.madeformaid.user.global.utils.CookieProvider
import jakarta.servlet.http.HttpServletResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user/admin")
class AdminController(
        private val adminService: AdminService,
        private val adminQueryService: AdminQueryService,
        private val cookieProvider: CookieProvider
) {
    @GetMapping("/profiles")
    fun getProfiles(): ResponseEntity<List<AdminProfileDTO>> {
        return ResponseEntity.ok(adminQueryService.getAdminProfiles(AuthContext.getAccountId()))
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

    @PostMapping("/system")
    fun createSystemAdmin(
        @RequestBody command: CreateSystemAdminCommand,
        response: HttpServletResponse
    ): ResponseEntity<AdminSignInResDTO> {
        val (signInResponse, refreshToken) = adminService.createSystemAdmin(command)

        response.setHeader(
            HttpHeaders.SET_COOKIE,
            cookieProvider.createRefreshTokenCookie(refreshToken).toString()
        )

        return ResponseEntity.ok(signInResponse)
    }

    @PostMapping("/owner")
    fun createOwner(
        @RequestBody command: CreateAdminCommand,
        response: HttpServletResponse
    ): ResponseEntity<AdminSignInResDTO> {
        val (signInResponse, refreshToken) = adminService.createShopOwner(
            command,
            AuthContext.getAccountId()
        )

        response.setHeader(
            HttpHeaders.SET_COOKIE,
            cookieProvider.createRefreshTokenCookie(refreshToken).toString()
        )

        return ResponseEntity.ok(signInResponse)
    }

    @PostMapping("/manager")
    fun createManager(
        @RequestBody command: CreateAdminCommand,
    ): ResponseEntity<String> =
        ResponseEntity.ok(
            adminService.createShopManager(
                command,
                AuthContext.getAccountId()
            )
        )

    @PostMapping("/staff")
    fun createStaf(
        @RequestBody command: CreateStaffCommand,
    ): ResponseEntity<String> =
        ResponseEntity.ok(
            adminService.createShopStaff(
                command,
                AuthContext.getAccountId()
            )
        )

    @GetMapping("/search")
    fun searchAdmins(
        query: SearchAdminQuery,
        pageable: Pageable
    ): ResponseEntity<Page<AdminDTO>> {
        val shopIdSettedQuery = query.copy(
            shopId = query.shopId ?: AuthContext.getShopId()
        )
        return ResponseEntity.ok(adminQueryService.searchAdmins(shopIdSettedQuery, pageable))
    }

    @PutMapping("/approve/{userId}")
    fun approveAdmin(
        @PathVariable userId: String,
    ): ResponseEntity<Void> {
        adminService.approveAdmin(userId, AuthContext.getUserId())

        return ResponseEntity.ok().build()
    }

    @PutMapping("/reject/{userId}")
    fun rejectAdmin(
        @PathVariable userId: String,
    ): ResponseEntity<Void> {
        adminService.rejectAdmin(userId, AuthContext.getUserId())

        return ResponseEntity.ok().build()
    }

    @GetMapping("/staff/{id}")
    fun getStaffDetail(@PathVariable id: String): ResponseEntity<StaffDetailDTO> =
        ResponseEntity.ok(adminQueryService.getStaffDetail(id))

    @PutMapping("/staff/introduction")
    fun updateStaffIntroduction(
        @RequestBody command: UpdateStaffIntroductionCommand,
    ): ResponseEntity<Void> {
        adminService.updateStaffIntroduction(command)
        return ResponseEntity.ok().build()
    }

    @PutMapping("/staff/concepts")
    fun updateStaffConcepts(
        @RequestBody command: UpdateStaffConceptsCommand
    ): ResponseEntity<Void> {
        adminService.updateStaffConcepts(command)
        return ResponseEntity.ok().build()
    }

    @PutMapping("/profile")
    fun updateProfile(
        @RequestBody command: UpdateProfileCommand
    ): ResponseEntity<Void> {
        adminService.updateProfile(command)
        return ResponseEntity.ok().build()
    }
}
