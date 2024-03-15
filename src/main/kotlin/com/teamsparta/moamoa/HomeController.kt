package com.teamsparta.moamoa

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class HomeController {
    @GetMapping("/start8080")
    fun home(): String {
        return "index"
    }
}
