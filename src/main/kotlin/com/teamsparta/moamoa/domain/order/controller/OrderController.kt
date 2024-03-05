package com.teamsparta.moamoa.domain.order.controller

import com.teamsparta.moamoa.domain.order.dto.CancelResponseDto
import com.teamsparta.moamoa.domain.order.dto.CreateOrderDto
import com.teamsparta.moamoa.domain.order.dto.ResponseOrderDto
import com.teamsparta.moamoa.domain.order.dto.UpdateOrderDto
import com.teamsparta.moamoa.domain.order.service.OrderService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/orders")
@RestController
@Validated
class OrderController(
    private val orderService: OrderService
) {

    @PostMapping("/{userId}/{productId}")
    fun createOrder(
        @PathVariable userId:Long,
        @PathVariable productId:Long,
        // @RequestParam으로 받으려 했으나 url에 명시 해주는거에 차이가 있다고 했다 userId는 jwt토큰 적용되면 아마도 해결될듯?
        @RequestBody createOrderDto: CreateOrderDto,
    ):ResponseEntity<ResponseOrderDto>{
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(orderService.creatOrder(userId,productId,createOrderDto))
    }

    @PutMapping("/{ordersId}/{userId}")
    fun updateOrder(
        @PathVariable ordersId:Long,
        @PathVariable userId: Long,
        @RequestBody updateOrderDto: UpdateOrderDto
    ):ResponseEntity<ResponseOrderDto>{
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(orderService.updateOrder(userId,ordersId,updateOrderDto))
    }

    @PutMapping("/cancel/{ordersId}/{userId}")
    fun cancelOrder(
        @PathVariable ordersId: Long,
        @PathVariable userId: Long,
    ):ResponseEntity<CancelResponseDto>{
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(orderService.cancelOrder(userId, ordersId))
    }

    //주문 취소니까 수정으로 함 삭제면 삭제지!

    @GetMapping("/{ordersId}/{userId}")
    fun gerOrder(
        @PathVariable ordersId: Long,
        @PathVariable userId: Long
    ):ResponseEntity<ResponseOrderDto>{
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(orderService.getOrder(userId,ordersId))
    }

    //주문 조회

    //주문 전체 조회


    // ---------------- sellerEntity 만들고 해~ -----------------------
    // 주문 추적상태 업데이트

    //판매 내역 조회

    // 판매 내역 전체 조회
}