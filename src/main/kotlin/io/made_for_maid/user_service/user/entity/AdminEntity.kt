package io.made_for_maid.user_service.user.entity

import io.made_for_maid.shared_lib.jpa.entity.BaseEntity
import io.made_for_maid.shared_lib.jpa.idGenerator.ShortId
import io.made_for_maid.shared_lib.vo.enums.AdminRole
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "admins")
class AdminEntity(
        @Id
        @ShortId
        val id: String,

        @Column(name = "nickname", nullable = false, length = 30)
        var nickname: String,

        @Column(name = "profile_image_url", length = 255)
        var profileImageUrl: String? = null,

        @Column(name = "maid_cafe_id", length = 100)
        val maidCafeId: String,

        @Column(name = "admin_role", nullable = false)
        @Enumerated(EnumType.STRING)
        val adminRole: AdminRole,

        @ManyToOne
        @JoinColumn(name = "account_id", nullable = false)
        val account: AccountEntity,
) : BaseEntity()
