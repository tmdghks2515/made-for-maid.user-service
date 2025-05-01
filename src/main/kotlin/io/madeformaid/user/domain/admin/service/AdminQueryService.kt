package io.madeformaid.user.domain.admin.service

import io.madeformaid.user.domain.admin.dto.data.AdminDTO
import io.madeformaid.user.domain.admin.dto.data.AdminProfileDTO
import io.madeformaid.user.domain.admin.dto.query.SearchAdminQuery
import io.madeformaid.user.domain.admin.mapper.AdminMapper
import io.madeformaid.user.domain.user.repository.UserRepository
import io.madeformaid.user.grpc.client.ShopNameFetcher
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
@Transactional(readOnly = true)
class AdminQueryService(
    private val userRepository: UserRepository,
    private val shopNameFetcher: ShopNameFetcher,
    private val adminMapper: AdminMapper,
) {
    fun getAdminProfiles(accountId: String): List<AdminProfileDTO> {
        val results = userRepository.findAdminsByAccountId(accountId)
            .map { adminMapper.toAdminProfileDTO(it) }

        check(results.isNotEmpty()) {
            "사용 가능한 프로필이 없습니다."
        }

        runCatching {
            results.map { it.shopId }
                .distinct().takeIf { it.isNotEmpty() }?.let { shopIds ->
                    val shopNameMap = shopNameFetcher.fetchShopNames(shopIds)
                    results.forEach {
                        it.shopName = shopNameMap[it.shopId]
                    }
                }
        }

        return results
    }

    fun searchAdmins(query: SearchAdminQuery, pageable: Pageable): Page<AdminDTO> {
        return userRepository.searchAdmins(query, pageable)
            .map { adminMapper.toAdminDTO(it) }
    }
}
