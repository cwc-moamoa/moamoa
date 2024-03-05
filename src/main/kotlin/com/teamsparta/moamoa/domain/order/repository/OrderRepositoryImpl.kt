package com.teamsparta.moamoa.domain.order.repository

import com.teamsparta.moamoa.domain.order.model.OrdersEntity
import com.teamsparta.moamoa.domain.order.model.QOrdersEntity
import com.teamsparta.moamoa.infra.QueryDslSupport
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.stereotype.Repository

@Repository
class OrderRepositoryImpl:CustomOrderRepository,QueryDslSupport() {
    private val orders = QOrdersEntity.ordersEntity
    override fun getOrderPage(userId: Long,page:Int,size:Int): Page<OrdersEntity> {
        val result = queryFactory.selectFrom(orders)
            .where(orders.userId.id.eq(userId))
            .offset(page.toLong())
            .limit(size.toLong())
            .orderBy(orders.userId.id.asc())
            .fetch()
        return PageImpl(result)
    }// 진행중 주문상태가 취소된 상품도 주문 완료도 뽑혀옴
}