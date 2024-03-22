package com.teamsparta.moamoa.domain.payment.controller

import com.siot.IamportRestClient.response.IamportResponse
import com.siot.IamportRestClient.response.Payment
import com.teamsparta.moamoa.domain.payment.dto.PaymentCallbackRequest
import com.teamsparta.moamoa.domain.payment.dto.RefundCallbackRequest
import com.teamsparta.moamoa.domain.payment.service.PaymentService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
@CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
class PaymentController(private val paymentService: PaymentService) {
    @GetMapping("/payment/{orderUid}")
    fun paymentPage(
        @PathVariable orderUid: String,
        model: Model,
    ): String {
        val requestDto = paymentService.findRequestDto(orderUid)
        model.addAttribute("requestDto", requestDto)
        return "payment"
    }

    @ResponseBody
    @PostMapping("/payment")
    fun validationPayment(
        @RequestBody request: PaymentCallbackRequest,
    ): ResponseEntity<IamportResponse<Payment>> {
        val iamportResponse = paymentService.paymentByCallback(request)

        return ResponseEntity.ok(iamportResponse)
    }

    @GetMapping("/success-payment")
    fun successPaymentPage(): String {
        return "success-payment"
    }

    @GetMapping("/fail-payment")
    fun failPaymentPage(): String {
        return "fail-payment"
    }

//    @ResponseBody
//    @PostMapping("/cancel")
//    fun refund(@RequestBody request: RefundCallbackRequest): ResponseEntity<IamportResponse<Payment>> {
//        val iamportResponse = paymentService.cancelReservation(request)
//
//        return ResponseEntity.ok(iamportResponse)
//    }
}
