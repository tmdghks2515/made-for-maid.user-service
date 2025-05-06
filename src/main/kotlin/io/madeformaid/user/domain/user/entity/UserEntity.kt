package io.madeformaid.user.domain.user.entity

import io.madeformaid.webmvc.jpa.entity.BaseSoftDeleteEntity
import io.madeformaid.webmvc.jpa.idGenerator.ShortId
import io.madeformaid.shared.vo.enums.Role
import io.madeformaid.user.global.vo.StaffConcept
import io.madeformaid.user.global.vo.StaffType
import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import java.time.LocalDateTime

@Entity
@Table(name = "users")
@SQLDelete(sql = "UPDATE user_db.users SET deleted_at = current_timestamp WHERE id = ?")
@SQLRestriction("deleted_at is null")
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

        @Enumerated(EnumType.STRING)
        @Column(name = "staff_type", columnDefinition = "varchar(100)")
        val staffType: StaffType? = null,

        @Column(name = "introduction", length = 300)
        var introduction: String? = null,

        @ElementCollection(fetch = FetchType.LAZY)
        @CollectionTable(
                name = "staff_concept",
                joinColumns = [JoinColumn(name = "user_id")]
        )
        @Enumerated(EnumType.STRING)
        @Column(name = "staff_concept", columnDefinition = "varchar(100)")
        var staffConcepts: MutableSet<StaffConcept> = mutableSetOf(),

        @ElementCollection(fetch = FetchType.LAZY)
        @CollectionTable(
                name = "user_role",
                joinColumns = [JoinColumn(name = "user_id")]
        )
        @Enumerated(EnumType.STRING)
        @Column(name = "role", columnDefinition = "varchar(100)")
        val roles: Set<Role> = emptySet(),

        @Enumerated(EnumType.STRING)
        @Column(name = "primary_role", columnDefinition = "varchar(100)")
        val primaryRole: Role,

        @Column(name = "approved_at")
        var approvedAt: LocalDateTime? = null,
) : BaseSoftDeleteEntity() {
        protected constructor() : this(
                nickname = "",
                roles = emptySet(),
                primaryRole = Role.USER,
        )

        fun isApprovalRequired(): Boolean {
                if (primaryRole == Role.USER) {
                        return false
                }

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

        fun approved() {
                check(isApprovalRequired()) { "승인이 필요 없는 사용자 프로필 입니다." }
                check(!isApproved()) { "이미 승인된 사용자 프로필 입니다." }
                approvedAt = LocalDateTime.now()
        }
}
