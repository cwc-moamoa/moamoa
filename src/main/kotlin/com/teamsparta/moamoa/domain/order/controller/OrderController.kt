package com.teamsparta.moamoa.domain.order.controller
import com.teamsparta.moamoa.domain.order.dto.*
import com.teamsparta.moamoa.domain.order.model.OrdersStatus
import com.teamsparta.moamoa.domain.order.service.OrderService
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
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
@Validated
class OrderController(
    private val orderService: OrderService,
) {
    @PostMapping("/{userId}/{productId}")
    fun createOrder(
        @PathVariable userId: Long,
        @PathVariable productId: Long,
        // @RequestParam으로 받으려 했으나 url에 명시 해주는거에 차이가 있다고 했다 userId는 jwt토큰 적용되면 아마도 해결될듯?
        @RequestBody createOrderDto: CreateOrderDto,
    ): ResponseEntity<ResponseOrderDto> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(orderService.creatOrder(userId, productId, createOrderDto))
    }

    @PutMapping("/{orderId}/{userId}")
    fun updateOrder(
        @PathVariable orderId: Long,
        @PathVariable userId: Long,
        @RequestBody updateOrderDto: UpdateOrderDto,
    ): ResponseEntity<ResponseOrderDto> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(orderService.updateOrder(userId, orderId, updateOrderDto))
    }

    @PutMapping("/cancel/{orderId}/{userId}")
    fun cancelOrder(
        @PathVariable orderId: Long,
        @PathVariable userId: Long,
    ): ResponseEntity<CancelResponseDto> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(orderService.cancelOrder(userId, orderId))
    }

    // 주문 취소니까 수정으로 함 삭제면 삭제지!

    @GetMapping("/{orderId}/{userId}")
    fun getOrder(
        @PathVariable orderId: Long,
        @PathVariable userId: Long,
    ): ResponseEntity<ResponseOrderDto> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(orderService.getOrder(userId, orderId))
    }

    // 주문 조회

    @GetMapping("/{userId}")
    fun getOrderPage(
        @PathVariable userId: Long,
        @RequestParam(value = "page", defaultValue = "1") page: Int,
        @RequestParam(value = "size", defaultValue = "2") size: Int,
    ): ResponseEntity<Page<ResponseOrderDto>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(orderService.getOrderPage(userId, page, size))
    }

    // 주문 전체 조회

    // ---------------- sellerEntity 만들고 해~ -----------------------
    // 주문 추적상태 업데이트
    @PutMapping("/status/{orderId}/{sellerId}")
    fun orderStatusChange(
        @PathVariable orderId: Long,
        @PathVariable sellerId: Long,
        @RequestParam status: OrdersStatus,
    ): ResponseEntity<ResponseOrderDto> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(orderService.orderStatusChange(orderId, sellerId, status))
    }
    // 주문상태 변경도 정보를 다 보여주는게 맞는거 같음

    @GetMapping("/seller{sellerId}/{orderId}")
    fun getOrderBySellerId(
        @PathVariable sellerId: Long,
        @PathVariable orderId: Long,
    ): ResponseEntity<ResponseOrderDto> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(orderService.getOrderBySellerId(sellerId, orderId))
    }

    // 판매 내역 조회
    @GetMapping("/seller/{sellerId}")
    fun getOrderPageBySellerId(
        @PathVariable sellerId: Long,
        @RequestParam(value = "page", defaultValue = "1") page: Int,
        @RequestParam(value = "size", defaultValue = "2") size: Int,
    ): ResponseEntity<Page<ResponseOrderDto>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(orderService.getOrderPageBySellerId(sellerId, page, size))
    }
    // 판매 내역 전체 조회
}
