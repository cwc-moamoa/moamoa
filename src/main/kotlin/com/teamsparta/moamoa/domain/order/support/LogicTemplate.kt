package com.teamsparta.moamoa.domain.order.support

class LogicTemplate {
    fun execute(callback: Callback) {
        // 로직 실행
        callback.call()
    }
}
