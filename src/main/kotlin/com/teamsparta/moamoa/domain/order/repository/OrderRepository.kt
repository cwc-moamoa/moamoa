package com.teamsparta.moamoa.domain.order.repository

import com.teamsparta.moamoa.domain.order.model.OrdersEntity
import com.teamsparta.moamoa.domain.product.model.Product
import com.teamsparta.moamoa.domain.seller.model.Seller
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

    fun findBySellerIdAndDeletedAtIsNull(sellerId: Long): List<OrdersEntity>


    fun findByIdAndDeletedAtIsNull(orderId: Long) : Optional<OrdersEntity>


}
