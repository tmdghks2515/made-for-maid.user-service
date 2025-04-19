package io.madeformaid.user.domain.user.entity

import io.madeformaid.shared.jpa.converter.EnumSetConverter
import io.madeformaid.shared.vo.enums.Role
import jakarta.persistence.Converter

@Converter
class RoleSetConverter : EnumSetConverter<Role>(Role::class.java)
