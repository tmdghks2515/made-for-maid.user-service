package io.madeformaid.user.domain.user.entity

import io.madeformaid.shared.jpa.entity.BaseEntity
import io.madeformaid.shared.jpa.idGenerator.ShortId
import io.madeformaid.shared.vo.enums.Role
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "users")
class UserEntity(
        @Id
        @ShortId
        var id: String? = null,

        @Column(name = "nickname", nullable = false, length = 30)
        var nickname: String,

        @Column(name = "profile_image_url")
        var profileImageUrl: String? = null,

        @Column(name = "cafe_id", length = 100)
        val cafeId: String? = null,

        @Column(name = "roles", nullable = false)
        @Convert(converter = RoleSetConverter::class)
        val roles: Set<Role>,

        @Column(name = "approved_at")
        var approvedAt: LocalDateTime? = null,

        @ManyToOne
        @JoinColumn(name = "account_id", nullable = false)
        var account: AccountEntity? = null,
) : BaseEntity()
