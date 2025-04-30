package io.madeformaid.user.domain.admin.dto.query

data class SearchAdminQuery(
    val accountId: String,
    val email: String? = null,
    val nickname: String? = null,
    val roles: Set<String>? = null,
    val primaryRole: String? = null,
    val shopId: String? = null,
    val staffType: String? = null,
    val staffConcepts: Set<String>? = null,
)
