package com.teamsparta.moamoa.domain.payment.service

import com.siot.IamportRestClient.response.IamportResponse
import com.siot.IamportRestClient.response.Payment
import com.teamsparta.moamoa.domain.payment.dto.PaymentCallbackRequest
import com.teamsparta.moamoa.domain.payment.dto.RefundCallbackRequest
import com.teamsparta.moamoa.domain.payment.dto.RequestPayDto

interface PaymentService {
    fun findRequestDto(orderUid: String): RequestPayDto

    // 결제(콜백)
    fun paymentByCallback(request: PaymentCallbackRequest): IamportResponse<Payment>?


//    fun cancelReservation(request: RefundCallbackRequest) : IamportResponse<Payment>
}
