package io.madeformaid.user.vo

import io.madeformaid.shared.vo.enums.DescribableEnum

enum class StaffConcept(
    private val displayName: String,
) : DescribableEnum {
    STAFF_CONCEPT_MAID("메이드"),
    STAFF_CONCEPT_BUTLER("집사"),
    STAFF_CONCEPT_ANGEL("천사"),
    STAFF_CONCEPT_DEMON("악마"),
    STAFF_CONCEPT_ANIMAL("동물"),
    STAFF_CONCEPT_COOL("쿨"),
    STAFF_CONCEPT_SHY("소심"),
    STAFF_CONCEPT_PURE("청순"),
    STAFF_CONCEPT_GANG("불량/양아치"),
    STAFF_CONCEPT_TSUNDERE("츤데레"),
    STAFF_CONCEPT_YANDERE("얀데레"),
    STAFF_CONCEPT_IDOL("아이돌"),
    STAFF_CONCEPT_BIG("빅"),
    STAFF_CONCEPT_SMALL("스몰"),
    STAFF_CONCEPT_MUSCLE("머슬"),
    STAFF_CONCEPT_OTHER("기타"),
    ;

    override fun getDisplayName(): String = displayName
}
