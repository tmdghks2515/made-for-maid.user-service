package io.made_for_maid.user_service.user.entity

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import io.made_for_maid.shared_lib.jpa.entity.BaseEntity
import io.made_for_maid.shared_lib.jpa.idGenerator.ShortId
import io.made_for_maid.shared_lib.vo.enums.OauthProvider
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

        @Column(name = "email", nullable = false, unique = true, length = 100)
        var email: String,

        @Column(name = "oauth_provider", length = 20)
        @Enumerated(EnumType.STRING)
        var oauthProvider: OauthProvider? = null,

        @Column(name = "oauth_id", length = 100, unique = true)
        var oauthId: String? = null,

        @Column(name = "is_active", nullable = false)
        var isActive: Boolean = true,

        @OneToMany(mappedBy = "account", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
        @JsonIgnoreProperties("account")
        val users: MutableSet<UserEntity> = mutableSetOf(),

        @OneToMany(mappedBy = "account", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
        @JsonIgnoreProperties("account")
        val admins: MutableSet<AdminEntity> = mutableSetOf(),
) : BaseEntity()
