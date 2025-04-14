package io.made_for_maid.user_service.user.entity

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import io.made_for_maid.shared_lib.jpa.entity.BaseEntity
import io.made_for_maid.shared_lib.jpa.idGenerator.ShortId
import jakarta.persistence.*

@Entity
@Table(name = "users")
class UserEntity(
        @Id
        @ShortId
        val id: String? = null,

        @Column(name = "nickname", nullable = false, length = 30)
        var nickname: String,

        @Column(name = "profile_image_url", length = 255)
        var profileImageUrl: String? = null,

        @Column(name = "maid_cafe_id", nullable = false, length = 100)
        val maidCafeId: String,

        @ManyToOne
        @JoinColumn(name = "account_id", nullable = false)
        val account: AccountEntity,
) : BaseEntity()
