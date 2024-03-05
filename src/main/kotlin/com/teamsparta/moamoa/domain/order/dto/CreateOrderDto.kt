package com.teamsparta.moamoa.domain.order.dto

data class CreateOrderDto(
    val address:String,
    val quantity:Int
)
//validated 로 quantity 값 잘 만져주기
