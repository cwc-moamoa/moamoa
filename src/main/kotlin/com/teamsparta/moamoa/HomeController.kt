package com.teamsparta.moamoa

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class HomeController {
    @GetMapping("/")
    fun home(): String {
        return "index"
    }

    @GetMapping("/order")
    fun goToOrderPage(
        @RequestParam productId: String,
    ): String {
        return "order"
    }

    @GetMapping("/groupOrder")
    fun goToGroupOrderPage(
        @RequestParam productId: String,
    ): String {
        return "groupOrder"
    }

    @GetMapping("/product-detail")
    fun goToProductDetail(
        @RequestParam productId: String,
    ): String {
        return "product-detail"
    }

    @GetMapping("/my-order-page")
    fun myOrderPage(): String {
        return "my-order-page"
    }

    @GetMapping("/my-order-page-detail")
    fun myOrderPageDetail(
        @RequestParam orderId: String,
    ): String {
        return "my-order-page-detail"
    }

}
