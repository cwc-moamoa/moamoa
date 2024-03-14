package com.teamsparta.moamoa.domain.payment.model

import jakarta.persistence.*

@Entity
@Table(name = "payments")
data class PaymentEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var price: Double,
    @Enumerated(EnumType.STRING)
    var status: PaymentStatus? = null,
    var paymentUid: String? = null, // 결제 고유 번호
) {
    fun changePaymentBySuccess(
        status: PaymentStatus,
        paymentUid: String,
    ) {
        this.status = status
        this.paymentUid = paymentUid
    }
}
