package com.teamsparta.moamoa.product.repository

import com.teamsparta.moamoa.infra.QueryDslSupport
import com.teamsparta.moamoa.product.model.Product
import com.teamsparta.moamoa.product.model.QProduct
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class CustomProductRepositoryImpl : QueryDslSupport(), CustomProductRepository {
    private val product = QProduct.product

    override fun findByPageable(pageable: Pageable): Page<Product> {
        val totalCount = queryFactory.select(product.count()).from(product).fetchOne() ?: 0L

        val query =
            queryFactory.selectFrom(product)
                .offset(pageable.offset)
                .limit(pageable.pageSize.toLong())

        if (pageable.sort.isSorted) {
            pageable.sort.forEach { sort ->
                val direction = sort.direction
                val property = sort.property

                when (property) {
                    "id" -> if (direction.isAscending) query.orderBy(product.id.asc()) else query.orderBy(product.id.desc())
                }
            }
        } else {
            query.orderBy(product.id.asc())
        }

        val contents = query.fetch()

        return PageImpl(contents, pageable, totalCount)
    }
}
