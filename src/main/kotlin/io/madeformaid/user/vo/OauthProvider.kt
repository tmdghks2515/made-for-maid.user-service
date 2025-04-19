package io.madeformaid.user.vo

import io.madeformaid.shared.vo.enums.DescribableEnum

enum class OauthProvider(
    private val displayName: String,
) : DescribableEnum {
    GOOGLE("구글"),
    KAKAO("카카오"),
    NAVER("네이버"),
    NONE("none"),
    ;

    override fun getDisplayName(): String = displayName
}
