package com.teamsparta.moamoa

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class HomeController {
    @GetMapping("/start8080")
    fun home(): String {
        return "index"
    }
    @GetMapping("/order")
    fun goToOrderPage(@RequestParam productId: String): String {
        return "order"
    }
}
