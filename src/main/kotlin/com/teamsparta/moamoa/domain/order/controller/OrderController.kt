package com.teamsparta.moamoa.domain.order.controller

import com.teamsparta.moamoa.domain.order.dto.CreateOrderDto
import com.teamsparta.moamoa.domain.order.dto.ResponseOrderDto
import com.teamsparta.moamoa.domain.order.service.OrderService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/orders")
@RestController
class OrderController(
    private val orderService: OrderService
) {

    @PostMapping("/{userId}")
    fun createOrder(
        @PathVariable userId:Long,productId:Long,
        // @RequestParam으로 받으려 했으나 url에 명시 해주는거에 차이가 있다고 했다 userId는 jwt토큰 적용되면 아마도 해결될듯?
        @RequestBody createOrderDto: CreateOrderDto,
    ):ResponseEntity<ResponseOrderDto>{
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(orderService.creatOrder(userId,productId,createOrderDto))
    }


}