package io.madeformaid.user.vo

import io.madeformaid.shared.vo.enums.DescribableEnum

enum class StaffType(
    private val displayName: String,
) : DescribableEnum {
    MAID("메이드"),
    BUTLER("집사"),
    ;

    override fun getDisplayName(): String = displayName
}
