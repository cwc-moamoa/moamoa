package com.teamsparta.moamoa.domain.payment.service

import com.siot.IamportRestClient.IamportClient
import com.siot.IamportRestClient.exception.IamportResponseException
import com.siot.IamportRestClient.request.CancelData
import com.siot.IamportRestClient.response.IamportResponse
import com.siot.IamportRestClient.response.Payment
import com.teamsparta.moamoa.domain.order.repository.OrderRepository
import com.teamsparta.moamoa.domain.order.service.OrderService
import com.teamsparta.moamoa.domain.payment.dto.PaymentCallbackRequest
import com.teamsparta.moamoa.domain.payment.dto.RequestPayDto
import com.teamsparta.moamoa.domain.payment.model.PaymentEntity
import com.teamsparta.moamoa.domain.payment.model.PaymentStatus
import com.teamsparta.moamoa.domain.payment.repository.PaymentRepository
import com.teamsparta.moamoa.domain.product.repository.ProductStockRepository
import com.teamsparta.moamoa.event.DiscountPaymentEvent
import jakarta.transaction.Transactional
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import java.io.IOException
import java.math.BigDecimal

@Service
class PaymentServiceImpl(
    private val orderRepository: OrderRepository,
    private val paymentRepository: PaymentRepository,
    private val iamportClient: IamportClient,
    private val orderService: OrderService,
    private val productStockRepository: ProductStockRepository,
    private val applicationEventPublisher: ApplicationEventPublisher,
) : PaymentService {
    @Transactional
    override fun findRequestDto(orderUid: String): RequestPayDto {
        val order =
            orderRepository.findOrderAndPaymentAndSocialUser(orderUid)
                .orElseThrow { IllegalArgumentException("주문이 없습니다.") }

//        val discountPrice = if (order.discount > 0.0) order.payment.price * (1 - order.discount / 100.0) else order.payment.price
// 바보야 왜 2중 할인 하고 있니 오더 에서 할인 다 했자너..

        return RequestPayDto(
            buyerName = order.socialUser.nickname,
            buyerAddress = order.address,
            paymentPrice = order.payment.price,
            itemName = order.productName,
            orderUid = order.orderUid!!,
            buyerPhone = order.phoneNumber,
        )
    }

    override fun paymentByCallback(request: PaymentCallbackRequest): IamportResponse<Payment> {
        try {
            // 결제 단건 조회(포트원) -> 포트원과 통신해서 고유UID 두개를 받아옴
            val iamportResponse = iamportClient.paymentByImpUid(request.paymentUid)

            val order =
                orderRepository.findOrderAndPayment(request.orderUid)
                    .orElseThrow { IllegalArgumentException("주문 내역이 없습니다.") }

            val stockFindAndPlus =
                productStockRepository.findById(order.product.id!!)
                    .orElseThrow { IllegalArgumentException("재고를 찾을 수 없습니다.") }

            if (iamportResponse.response.status != "paid") {
                orderRepository.delete(order)
                paymentRepository.delete(order.payment)
                stockFindAndPlus.stock += order.quantity
                productStockRepository.save(stockFindAndPlus)

//                return IamportResponse()
                throw RuntimeException("결제 미완료")
//                println("결제 미완료")
            }

            val price = order.payment.price

            val iamportPrice = iamportResponse.response.amount.toDouble()

            if (iamportPrice != price) {
                orderRepository.delete(order)
                paymentRepository.delete(order.payment)
                stockFindAndPlus.stock += order.quantity
                productStockRepository.save(stockFindAndPlus)

                // 결제금액 위변조로 의심되는 결제금액을 취소(포트원)
                iamportClient.cancelPaymentByImpUid(
                    CancelData(
                        iamportResponse.response.impUid,
                        true,
                        BigDecimal(iamportPrice),
                    ),
                )

                throw RuntimeException("결제금액 위변조 의심")
//                println("결제 금액 위변조 의심")
            }

            order.payment.changePaymentBySuccess(PaymentStatus.OK, iamportResponse.response.impUid)
//            val paymentChange =
//                PaymentEntity(
//                    paymentUid = iamportResponse.response.impUid,
//                    status = PaymentStatus.OK,
//                    price = price,
//                )
//            paymentRepository.save(paymentChange)

            if (order.discount > 0.0) {
                orderService.saveToRedis(order.product.id.toString(), order.socialUser.id.toString(), order.id.toString())
                val discountAppliedEvent = DiscountPaymentEvent(order.id.toString(), order.socialUser.id.toString())
                applicationEventPublisher.publishEvent(discountAppliedEvent)
            }
            return iamportResponse
        } catch (e: IamportResponseException) {
            throw RuntimeException(e)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }
}
