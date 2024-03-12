package com.teamsparta.moamoa.domain.order.repository

import com.teamsparta.moamoa.domain.order.model.OrdersEntity
import com.teamsparta.moamoa.domain.order.model.QOrdersEntity
import com.teamsparta.moamoa.domain.product.model.QProduct
import com.teamsparta.moamoa.domain.seller.model.QSeller
import com.teamsparta.moamoa.infra.QueryDslSupport
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl

class OrderRepositoryImpl : CustomOrderRepository, QueryDslSupport() {
    private val orders = QOrdersEntity.ordersEntity
    private val product = QProduct.product
    private val seller = QSeller.seller

    override fun getOrderPage(
        userId: Long,
        page: Int,
        size: Int,
    ): Page<OrdersEntity> {
        val result =
            queryFactory.selectFrom(orders)
                .where(orders.user.id.eq(userId))
                .offset((page - 1).toLong())
                .limit(size.toLong())
                .orderBy(orders.user.id.asc())
                .fetch()
        return PageImpl(result)
    }

    override fun getOrderPageBySellerId(
        sellerId: Long,
        page: Int,
        size: Int,
    ): Page<OrdersEntity> {
        val join =
            queryFactory.selectFrom(orders)
                .join(orders.product, product).fetchJoin()
                .where(seller.id.eq(sellerId))
        val result =
            join
                .offset((page - 1).toLong())
                .limit(size.toLong())
                .orderBy(orders.product.id.asc())
                .fetch()
        return PageImpl(result)
    }
}
