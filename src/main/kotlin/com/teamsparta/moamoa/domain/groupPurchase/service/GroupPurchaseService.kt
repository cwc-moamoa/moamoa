package com.teamsparta.moamoa.domain.groupPurchase.service

interface GroupPurchaseService {
    fun createAndJoinOrJoinGroupPurchase(userId:String,orderId:String)

    fun joinGroupPurchase(userId: Long,groupPurchaseId:Long,orderId:Long)

    fun leaveGroupPurchase(userId: Long,groupPurchaseId: Long)
}