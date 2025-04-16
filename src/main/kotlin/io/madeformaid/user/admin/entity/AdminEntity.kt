package io.madeformaid.user.admin.entity

import io.madeformaid.shared.jpa.entity.BaseEntity
import io.madeformaid.shared.jpa.idGenerator.ShortId
import io.madeformaid.shared.vo.enums.AdminRole
import io.madeformaid.user.user.entity.AccountEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "admins")
class AdminEntity(
        @Id
        @ShortId
        var id: String? = null,

        @Column(name = "nickname", nullable = false, length = 30)
        var nickname: String,

        @Column(name = "profile_image_url", length = 255)
        var profileImageUrl: String? = null,

        @Column(name = "maid_cafe_id", length = 100)
        val maidCafeId: String? = null,

        @Column(name = "admin_role", nullable = false)
        @Enumerated(EnumType.STRING)
        val adminRole: AdminRole,

        @Column(name = "approved_at")
        var approvedAt: LocalDateTime? = null,

        @ManyToOne
        @JoinColumn(name = "account_id", nullable = false)
        var account: AccountEntity? = null,
) : BaseEntity()
