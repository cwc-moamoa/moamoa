package com.teamsparta.moamoa.event

import com.teamsparta.moamoa.domain.groupPurchase.service.GroupPurchaseService
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class DiscountPaymentEventListener(private val groupPurchaseService: GroupPurchaseService) {
    @Async
    @EventListener
    fun handleDiscountAppliedEvent(event: DiscountPaymentEvent) {
        groupPurchaseService.createAndJoinOrJoinGroupPurchase(event.orderId, event.userId)
    }
}
