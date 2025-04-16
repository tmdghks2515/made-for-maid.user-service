package io.madeformaid.user.user.entity

import io.madeformaid.shared.jpa.entity.BaseEntity
import io.madeformaid.shared.jpa.idGenerator.ShortId
import jakarta.persistence.*

@Entity
@Table(name = "users")
class UserEntity(
        @Id
        @ShortId
        var id: String? = null,

        @Column(name = "nickname", nullable = false, length = 30)
        var nickname: String,

        @Column(name = "profile_image_url", length = 255)
        var profileImageUrl: String? = null,

        @Column(name = "maid_cafe_id", nullable = false, length = 100)
        val maidCafeId: String,

        @ManyToOne
        @JoinColumn(name = "account_id", nullable = false)
        var account: AccountEntity? = null,
) : BaseEntity()
