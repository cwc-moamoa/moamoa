package com.teamsparta.moamoa.product.repository

import com.teamsparta.moamoa.infra.QueryDslSupport
import com.teamsparta.moamoa.product.model.Product
import com.teamsparta.moamoa.product.model.QProduct
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository// 페이징 아직 오류 해결못함
class CustomProductRepositoryImpl : QueryDslSupport(), CustomProductRepository {
    private val product = QProduct.product

    override fun findByPageable(pageable: Pageable): Page<Product> {
        // 전체 상품 수를 조회
        val totalCount = queryFactory.select(product.count()).from(product).fetchOne() ?: 0L

        // QueryDSL 쿼리를 생성
        val query =
            queryFactory.selectFrom(product)
                .offset(pageable.offset)
                .limit(pageable.pageSize.toLong())

        // Sort 조건이 있는 경우, 해당 조건으로 정렬하고, 없는 경우 id로 오름차순 정렬
        if (pageable.sort.isSorted) {
            pageable.sort.forEach { sort ->
                val direction = sort.direction
                val property = sort.property

                when (property) {
                    "id" -> if (direction.isAscending) query.orderBy(product.id.asc()) else query.orderBy(product.id.desc())
                    // 필요한 필드에 대해서 추가 가능
                }
            }
        } else {
            query.orderBy(product.id.asc())
        }

        // 쿼리를 실행하여 결과를 가져옴
        val contents = query.fetch()

        // PageImpl 객체를 생성하여 반환
        return PageImpl(contents, pageable, totalCount)
    }
}
