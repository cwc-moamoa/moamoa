package com.teamsparta.moamoa.domain.order.dto

import jakarta.validation.constraints.NotNull

data class UpdateOrderDto(
    @field:NotNull
    val address: String,
)
// 일단은 주소만 변경
