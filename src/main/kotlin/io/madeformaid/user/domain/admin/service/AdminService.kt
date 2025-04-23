package io.madeformaid.user.domain.admin.service

import io.madeformaid.webmvc.context.AuthContext
import io.madeformaid.webmvc.exception.BusinessException
import io.madeformaid.webmvc.exception.ErrorCode
import io.madeformaid.user.domain.admin.dto.data.AdminProfileDTO
import io.madeformaid.user.domain.admin.dto.data.AdminSignInResDTO
import io.madeformaid.user.domain.admin.mapper.AdminMapper
import io.madeformaid.user.domain.user.repository.AccountRepository
import io.madeformaid.user.domain.user.repository.UserRepository
import io.madeformaid.user.utils.JwtTokenProvider
import io.madeformaid.user.vo.SignInResStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class AdminService(
        private val accountRepository: AccountRepository,
        private val userRepository: UserRepository,
        private val adminMapper: AdminMapper,
        private val jwtTokenProvider: JwtTokenProvider,
) {
    @Transactional(readOnly = true)
    fun getAdminProfiles(accountId: String): List<AdminProfileDTO> {
        return userRepository.findAdminsByAccountId(accountId)
                .map { adminMapper.toAdminProfileDTO(it) }
    }

    fun selectProfile(userId: String): Pair<AdminSignInResDTO, String> {
        val account = accountRepository.findById(AuthContext.getAccountId())
                .orElseThrow { BusinessException(ErrorCode.NOT_FOUND) }
        val admin = account.users.find { it.id == userId } ?: throw BusinessException(ErrorCode.NOT_FOUND)

        checkNotNull(admin.approvedAt) { "승인되지 않은 프로필 입니다. 사장님에게 승인을 요청해주세요." }

        val adminDTO = adminMapper.toAdminDTO(admin)

        return AdminSignInResDTO(
                status = SignInResStatus.SIGN_IN_SUCCESS,
                accessToken = jwtTokenProvider.createAccessToken(adminDTO),
                admin = adminDTO,
        ) to jwtTokenProvider.createRefreshToken(adminDTO)
    }
}
