package com.teamsparta.moamoa.domain.fixture

import com.siot.IamportRestClient.response.payco.OrderStatus
import com.teamsparta.moamoa.domain.order.model.OrdersEntity
import com.teamsparta.moamoa.domain.payment.model.PaymentEntity
import com.teamsparta.moamoa.domain.payment.model.PaymentStatus

class CreateOrdersEntityFixture {
    companion object {
        fun createOrder() : OrdersEntity {
            return OrdersEntity(
                productName = "productName",
                totalPrice = 1000.0,
                address = "address",
                discount = 0.0,
                phoneNumber = "phoneNumber",
                orderUid = "orderUid",
                product = CreateProductFixture.createProduct(),
                socialUser = CreateSocialUserFixture.createSocialUser(),
                sellerId = 1L,
                quantity = 1,
                payment = PaymentEntity(
                    id = 1L,
                    paymentUid = "paymentUid",
                    price = 1000.0,
                    status = PaymentStatus.OK,
                ),
            )
        }
    }
}