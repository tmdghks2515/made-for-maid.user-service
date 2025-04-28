package io.madeformaid.user.domain.user.entity

import io.madeformaid.webmvc.jpa.entity.BaseEntity
import io.madeformaid.webmvc.jpa.idGenerator.ShortId
import io.madeformaid.shared.vo.enums.Role
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "users")
class UserEntity(
        @Id
        @ShortId
        var id: String? = null,

        @ManyToOne
        @JoinColumn(name = "account_id", nullable = false)
        var account: AccountEntity? = null,

        @Column(name = "shop_id", length = 100)
        val shopId: String? = null,

        @Column(name = "nickname", nullable = false, length = 30)
        var nickname: String,

        @Column(name = "profile_image_url")
        var profileImageUrl: String? = null,

        @ElementCollection(fetch = FetchType.LAZY)
        @CollectionTable(
                name = "user_role",
                joinColumns = [JoinColumn(name = "user_id")]
        )
        @Enumerated(EnumType.STRING)
        @Column(name = "role", columnDefinition = "varchar(20)")
        val roles: Set<Role>,

        @Column(name = "approved_at")
        var approvedAt: LocalDateTime? = null,
) : BaseEntity() {
        protected constructor() : this(
                nickname = "",
                roles = emptySet(),
        )

        fun isApprovalRequired(): Boolean {
                val nonApprovalRoles = setOf(Role.SUPER_ADMIN, Role.SYSTEM_ADMIN, Role.SHOP_OWNER)
                roles.forEach { role ->
                        if (nonApprovalRoles.contains(role)) {
                                return false
                        }
                }
                return true
        }

        fun isApproved(): Boolean {
                return !isApprovalRequired() || approvedAt != null
        }
}
