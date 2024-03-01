package com.teamsparta.moamoa.domain.groupPurchase.controller

import com.teamsparta.moamoa.domain.groupPurchase.dto.CreateGroupPurchaseRequest
import com.teamsparta.moamoa.domain.groupPurchase.dto.GroupPurchaseJoinUserResponse
import com.teamsparta.moamoa.domain.groupPurchase.dto.GroupPurchaseResponse
import com.teamsparta.moamoa.domain.groupPurchase.dto.JoinGroupPurchaseRequest
import com.teamsparta.moamoa.domain.groupPurchase.service.GroupPurchaseService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/groupPurchases")
class GroupPurchaseController(private val groupPurchaseService: GroupPurchaseService)
{

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
        @RequestParam userId: Long
    ): ResponseEntity<String> {
        groupPurchaseService.createAndJoinOrJoinGroupPurchase(request, userId)
        return ResponseEntity.status(HttpStatus.OK).body("공동구매 매칭 성공")
    }


    @PostMapping("/join")
    fun join(
        @RequestParam userId: Long,
        @RequestParam groupPurchaseId: Long,
    ): ResponseEntity<Unit> {
        val groupPurchaseJoinUserResponse = groupPurchaseService.joinGroupPurchase(userId, groupPurchaseId)
        return ResponseEntity.status(HttpStatus.OK).body(groupPurchaseJoinUserResponse)
    }


    @PostMapping("/leave")
    fun leave(
        @RequestParam userId: Long,
        @RequestParam groupPurchaseId: Long,
    ) : ResponseEntity<Unit> {
        groupPurchaseService.leaveGroupPurchase(userId, groupPurchaseId)
        return ResponseEntity.status(HttpStatus.OK).build()
    }

}