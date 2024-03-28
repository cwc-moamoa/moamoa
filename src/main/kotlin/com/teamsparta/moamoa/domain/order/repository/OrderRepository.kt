package com.teamsparta.moamoa.domain.order.repository

import com.teamsparta.moamoa.domain.order.model.OrdersEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface OrderRepository : JpaRepository<OrdersEntity, Long>, CustomOrderRepository {
    @Query(
        "select o from OrdersEntity o" +
            " left join fetch o.payment p" +
            " left join fetch o.socialUser m" +
            " where o.orderUid = :orderUid",
    )
    fun findOrderAndPaymentAndSocialUser(orderUid: String): Optional<OrdersEntity>

    @Query(
        "select o from OrdersEntity o" +
            " left join fetch o.payment p" +
            " where o.orderUid = :orderUid",
    )
    fun findOrderAndPayment(orderUid: String): Optional<OrdersEntity>

    fun findByProductIdAndSocialUserId(
        productId: Long,
        socialUserId: Long?,
    ): Optional<OrdersEntity>

    fun findByIdAndDeletedAtIsNull(orderId: Long): Optional<OrdersEntity>

    fun findBySellerIdAndDeletedAtIsNull(sellerId: Long): List<OrdersEntity>

    // 주문당 리뷰가 상품당 주문당 리뷰로 바꿈
    fun findByIdAndSocialUserIdAndProductId(orderId: Long, socialUserId: Long, productId: Long): Optional<OrdersEntity>

}
