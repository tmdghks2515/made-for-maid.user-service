package io.made_for_maid.user_service.user.service

import io.made_for_maid.shared_lib.vo.enums.OauthProvider
import io.made_for_maid.user_service.user.dto.command.SignUpCommand
import io.made_for_maid.user_service.user.entity.AccountEntity
import io.made_for_maid.user_service.user.repository.AccountRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class AccountService(
        private val accountRepository: AccountRepository,
) {
    fun signUpByKakao(command: SignUpCommand): String {
        val accountEntity = AccountEntity(
                email = command.email,
                oauthProvider = OauthProvider.KAKAO,
                oauthId = command.oauthId,
                isActive = true,
        )

        return accountRepository.save(accountEntity).id ?: throw IllegalStateException("Registered Account ID cannot be null")
    }
}
