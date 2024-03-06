package com.teamsparta.moamoa.domain.groupPurchase.controller

import com.teamsparta.moamoa.domain.groupPurchase.dto.CreateGroupPurchaseRequest
import com.teamsparta.moamoa.domain.groupPurchase.service.GroupPurchaseService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/groupPurchases")
class GroupPurchaseController(private val groupPurchaseService: GroupPurchaseService) {
    //    @PostMapping("/createAndJoin")
//    fun createAndJoin(
//        @RequestBody request: CreateGroupPurchaseRequest,
//        @RequestParam userId: Long,
//    ): ResponseEntity<GroupPurchaseResponse> {
//        val groupPurchaseResponse = groupPurchaseService.createAndJoinGroupPurchase(request, userId)
//        return ResponseEntity.status(HttpStatus.OK).body(groupPurchaseResponse)
//    }

    @PostMapping("/createAndJoin")
    fun createAndJoinOrJoin(
        @RequestBody request: CreateGroupPurchaseRequest,
        @RequestParam userId: Long,
        @RequestParam orderId: Long,
    ): ResponseEntity<String> {
        groupPurchaseService.createAndJoinOrJoinGroupPurchase(request, userId, orderId)
        return ResponseEntity.status(HttpStatus.OK).body("공동구매 매칭 성공")
    }

    @PostMapping("/join")
    fun join(
        @RequestParam userId: Long,
        @RequestParam groupPurchaseId: Long,
        @RequestParam orderId: Long,
    ): ResponseEntity<Unit> {
        val groupPurchaseJoinUserResponse = groupPurchaseService.joinGroupPurchase(userId, groupPurchaseId, orderId)
        return ResponseEntity.status(HttpStatus.OK).body(groupPurchaseJoinUserResponse)
    }

    @PutMapping("/leave")
    fun leave(
        @RequestParam userId: Long,
        @RequestParam groupPurchaseId: Long,
    ): ResponseEntity<Unit> {
        groupPurchaseService.leaveGroupPurchase(userId, groupPurchaseId)
        return ResponseEntity.status(HttpStatus.OK).build()
    }

    @PostMapping("/saveToRedis")
    fun saveDataToRedis(
        @RequestParam productId: String,
        @RequestParam userId: String,
        @RequestParam orderId: String,
    ): ResponseEntity<Unit> {
        groupPurchaseService.saveToRedis(productId, userId, orderId) // 여기 순서 중요함!!
        return ResponseEntity.status(HttpStatus.OK).build()
    }
}
