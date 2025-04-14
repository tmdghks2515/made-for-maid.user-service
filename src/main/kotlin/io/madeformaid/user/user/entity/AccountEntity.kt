package io.madeformaid.user.user.entity

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import io.madeformaid.shared.jpa.entity.BaseEntity
import io.madeformaid.shared.jpa.idGenerator.ShortId
import io.madeformaid.shared.vo.enums.AdminRole
import io.madeformaid.shared.vo.enums.OauthProvider
import jakarta.persistence.*
import org.hibernate.annotations.Filter
import org.hibernate.annotations.FilterDef

@Entity
@Table(name = "account")
@FilterDef(name = "activeFilter", defaultCondition = "is_active = true")
@Filter(name = "activeFilter")
class AccountEntity(
        @Id
        @ShortId
        val id: String? = null,

        @Column(name = "email", nullable = false, length = 100)
        var email: String,

        @Column(name = "oauth_provider", length = 20)
        @Enumerated(EnumType.STRING)
        var oauthProvider: OauthProvider? = null,

        @Column(name = "oauth_id", length = 100, unique = true)
        var oauthId: String? = null,

        @Column(name = "user_recent_maid_cafe_id", length = 100)
        var userRecentMaidCafeId: String? = null,

        @Column(name = "admin_recent_maid_cafe_id", length = 100)
        var adminRecentMaidCafeId: String? = null,

        @Column(name = "is_active", nullable = false)
        var isActive: Boolean = true,

        @OneToMany(mappedBy = "account", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
        @JsonIgnoreProperties("account")
        val users: MutableSet<UserEntity> = mutableSetOf(),

        @OneToMany(mappedBy = "account", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
        @JsonIgnoreProperties("account")
        val admins: MutableSet<AdminEntity> = mutableSetOf(),
) : BaseEntity() {
        fun addUser(user: UserEntity) {
                if (users.any { it.maidCafeId == user.maidCafeId }) {
                        throw IllegalArgumentException("동일한 카페에 이미 가입된 계정이 존재합니다.")
                }

                users.add(user)
                user.account = this

                userRecentMaidCafeId = user.maidCafeId
        }

        fun addMaidCafeAdmin(admin: AdminEntity) {
                if (admins.any { it.maidCafeId == admin.maidCafeId && it.adminRole == AdminRole.MAID_CAFE_ADMIN}) {
                        throw IllegalArgumentException("동일한 카페에 이미 가입된 관리자 계정이 존재합니다.")
                }

                admins.add(admin)
                admin.account = this

                adminRecentMaidCafeId = admin.maidCafeId
        }

        fun addSystemAdmin(admin: AdminEntity) {
                if (admins.any { it.adminRole == AdminRole.SYSTEM_ADMIN }) {
                        throw IllegalArgumentException("이미 시스템 관리자 계정이 존재합니다.")
                }

                admins.add(admin)
                admin.account = this
        }

        fun getRecentSignedInUser(): UserEntity? =
                userRecentMaidCafeId?.let { userRecentMaidCafeId ->
                        users.find { it.maidCafeId == userRecentMaidCafeId }
                }

        fun getRecentSignedInMaidCafeAdmin(): AdminEntity? =
                adminRecentMaidCafeId?.let { adminRecentMaidCafeId ->
                        admins.find { it.maidCafeId == adminRecentMaidCafeId && it.adminRole == AdminRole.MAID_CAFE_ADMIN }
                }

        fun getSystemAdmin(): AdminEntity? {
                return admins.find { it.adminRole == AdminRole.SYSTEM_ADMIN }
        }
}
