package io.madeformaid.user.domain.user.entity

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import io.madeformaid.webmvc.jpa.entity.BaseEntity
import io.madeformaid.webmvc.jpa.idGenerator.ShortId
import io.madeformaid.user.global.vo.OauthProvider
import io.madeformaid.shared.vo.enums.Role
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
        var id: String? = null,

        @Column(name = "recent_user_id", length = 100)
        var recentUserId: String? = null,

        @Column(name = "recent_admin_id", length = 100)
        var recentAdminId: String? = null,

        @Column(name = "email", nullable = false, length = 100)
        var email: String,

        @Column(name = "oauth_provider", columnDefinition = "varchar(100)")
        @Enumerated(EnumType.STRING)
        var oauthProvider: OauthProvider? = null,

        @Column(name = "oauth_id", unique = true)
        var oauthId: String? = null,

        @Column(name = "is_active", nullable = false)
        var isActive: Boolean = true,

        @OneToMany(mappedBy = "account", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
        @JsonIgnoreProperties("account")
        val users: MutableSet<UserEntity> = mutableSetOf(),
) : BaseEntity() {
        protected constructor() : this(
                email = "",
        )

        fun addUser(user: UserEntity) {
                if (users.any { it.shopId == user.shopId }) {
                        throw IllegalArgumentException("동일한 카페에 이미 가입된 계정이 존재합니다.")
                }

                users.add(user)
                user.account = this
        }

        fun addShopOwner(admin: UserEntity) {
                if (users.any { it.shopId == admin.shopId && it.primaryRole == Role.SHOP_OWNER }) {
                        throw IllegalArgumentException("이미 가입된 사장님 계정이 존재합니다.")
                }

                users.add(admin)
                admin.account = this
        }

        fun addShopStaff(admin: UserEntity) {
                if (users.any { it.shopId == admin.shopId && it.primaryRole == Role.SHOP_STAFF }) {
                        throw IllegalArgumentException("이미 가입된 메이드/집사 계정이 존재합니다.")
                }

                requireNotNull(admin.staffType) { "메이드 or 집사를 선택해주세요."}
                require(admin.staffConcepts?.isNotEmpty() == true) { "컨셉 유형을 최소 하나 이상 선택해주세요."}

                users.add(admin)
                admin.account = this
        }

        fun addShopManager(admin: UserEntity) {
                if (users.any { it.shopId == admin.shopId && it.primaryRole == Role.SHOP_MANAGER }) {
                        throw IllegalArgumentException("이미 가입된 매니저 계정이 존재합니다.")
                }

                users.add(admin)
                admin.account = this
        }

        fun addSystemAdmin(admin: UserEntity) {
                if (users.any { it.roles.contains(Role.SYSTEM_ADMIN) }) {
                        throw IllegalArgumentException("이미 시스템 관리자 계정이 존재합니다.")
                }

                users.add(admin)
                admin.account = this
        }

        fun getRecentSignedInUser(): UserEntity? =
                recentUserId?.let { recentUserId ->
                        users.find { it.id == recentUserId }
                }

        fun getRecentSignedInAdmin(): UserEntity? =
                recentAdminId?.let { recentAdminId ->
                        users.find { it.id == recentAdminId }
                }

        fun getSystemAdmin(): UserEntity? =
                users.find { it.roles.contains(Role.SYSTEM_ADMIN) || it.roles.contains(Role.SUPER_ADMIN) }

        fun getAdmins(): List<UserEntity> =
                users.filter { it.roles.contains(Role.SHOP_OWNER) ||
                        it.roles.contains(Role.SHOP_MANAGER) ||
                        it.roles.contains(Role.SHOP_STAFF) ||
                        it.roles.contains(Role.SUPER_ADMIN) ||
                        it.roles.contains(Role.SYSTEM_ADMIN)
                }
}
