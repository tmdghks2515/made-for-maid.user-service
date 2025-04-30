package io.madeformaid.user.domain.user.repository.custom.impl

import com.querydsl.jpa.impl.JPAQueryFactory
import io.madeformaid.shared.vo.enums.Role
import io.madeformaid.user.domain.admin.dto.data.AdminDTO
import io.madeformaid.user.domain.admin.dto.query.SearchAdminQuery
import io.madeformaid.user.domain.user.entity.QAccountEntity
import io.madeformaid.user.domain.user.entity.QUserEntity
import io.madeformaid.user.domain.user.entity.UserEntity
import io.madeformaid.user.domain.user.repository.custom.UserRepositoryCustom
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory,
) : UserRepositoryCustom {

    override fun searchAdmins(
        query: SearchAdminQuery,
        pageable: Pageable
    ): Page<UserEntity> {
        val qUser = QUserEntity.userEntity
        val qAccount = QAccountEntity.accountEntity

        val conditions = listOfNotNull(
            qUser.primaryRole.`in`(listOf(Role.SHOP_OWNER, Role.SHOP_MANAGER, Role.SHOP_STAFF)),
            query.shopId?.let { qUser.shopId.eq(it) },
            query.nicknameLike?.let { qUser.nickname.containsIgnoreCase(it) },
            query.primaryRoles?.takeIf { it.isNotEmpty() }?.let { qUser.primaryRole.`in`(it) },
            query.staffType?.let { qUser.staffType.eq(it) },
        ).toTypedArray()

        val orders = pageable.sort.toList().mapNotNull { order ->
            val property = order.property
            val direction = order.direction

            when (property) {
                "createdAt" -> if (direction.isAscending) qUser.createdAt.asc() else qUser.createdAt.desc()
                "nickname" -> if (direction.isAscending) qUser.nickname.asc() else qUser.nickname.desc()
                else -> null // 알 수 없는 필드는 무시
            }
        }.toTypedArray()

        val content = queryFactory
            .selectFrom(qUser)
            .join(qUser.account, qAccount)
            .distinct()
            .where(*conditions)
            .orderBy(*orders)
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val count = queryFactory
            .select(qUser.countDistinct())
            .from(qUser)
            .where(*conditions)
            .fetchOne() ?: 0L

        return PageImpl(content, pageable, count)
    }
}
