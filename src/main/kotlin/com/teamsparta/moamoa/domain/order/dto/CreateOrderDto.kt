package com.teamsparta.moamoa.domain.order.dto

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Pattern

data class CreateOrderDto(
    val productId:Long,
    val address: String,
    @field:Min( value = 1, message = "1이상 주문 가능")
    @field:Max( value = 99, message = "99개 이상 주문 불가")
    val quantity: Int,
    @field:Pattern(regexp = "^01(?:0|1|[6-9])(\\d{3,4})(\\d{4})$", message = "핸드폰 번호 양식으로 입력해 주세요")
    val phoneNumber: String
)
// validated 로 quantity 값 잘 만져주기
