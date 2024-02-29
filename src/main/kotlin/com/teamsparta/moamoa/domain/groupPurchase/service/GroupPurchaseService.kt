package com.teamsparta.moamoa.domain.groupPurchase.service

import com.teamsparta.moamoa.domain.groupPurchase.repository.GroupPurchaseJoinUserRepository
import com.teamsparta.moamoa.domain.groupPurchase.repository.GroupPurchaseRepository
import org.springframework.stereotype.Service
import com.teamsparta.moamoa.domain.groupPurchase.dto.*
import com.teamsparta.moamoa.domain.groupPurchase.model.GroupPurchaseEntity
import com.teamsparta.moamoa.domain.groupPurchase.model.GroupPurchaseJoinUserEntity
import com.teamsparta.moamoa.exception.ModelNotFoundException
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull

@Service
class GroupPurchaseService(
    private val groupPurchaseRepository: GroupPurchaseRepository,
    private val groupPurchaseJoinUserRepository: GroupPurchaseJoinUserRepository,
) {

//    @Transactional
//    fun createGroupPurchase(request: CreateGroupPurchaseRequest): GroupPurchaseResponse {
//        val groupPurchase = GroupPurchaseEntity(
//            request.productId, request.userLimit, 0, request.timeLimit, request.discount, mutableListOf()
//        )
//        val saveGroupPurchase = groupPurchaseRepository.save(groupPurchase)
//        return saveGroupPurchase.toResponse()
//    }

    @Transactional
    fun createAndJoinGroupPurchase(request: CreateGroupPurchaseRequest , userId: Long ): GroupPurchaseResponse {
        val groupPurchase = GroupPurchaseEntity(
            request.productId, request.userLimit, 1, request.timeLimit, request.discount, mutableListOf()
        )

        val groupPurchaseJoinUser = GroupPurchaseJoinUserEntity(userId, groupPurchase)

        groupPurchase.groupPurchaseUsers.add(groupPurchaseJoinUser)

        val savedGroupPurchase = groupPurchaseRepository.save(groupPurchase)

        return savedGroupPurchase.toResponse()
    }


    @Transactional
    fun joinGroupPurchase(userId: Long, groupPurchaseId: Long): GroupPurchaseJoinUserResponse {
        val groupPurchase = groupPurchaseRepository.findByIdOrNull(groupPurchaseId) ?: throw ModelNotFoundException(
            "GroupPurchase", groupPurchaseId
        )
        if (groupPurchase.userCount >= groupPurchase.userLimit) {
            throw IllegalStateException("GroupPurchase is full")
        }
        val groupPurchaseJoinUser = GroupPurchaseJoinUserEntity(userId, groupPurchase)
        groupPurchase.groupPurchaseUsers.add(groupPurchaseJoinUser)
        groupPurchase.userCount++

        return groupPurchaseJoinUserRepository.save(groupPurchaseJoinUser).toResponse()
    }


    @Transactional
    fun leaveGroupPurchase(userId: Long, groupPurchaseId: Long) {
        val groupPurchase = groupPurchaseRepository.findByIdOrNull(groupPurchaseId) ?: throw ModelNotFoundException(
            "GroupPurchase", groupPurchaseId
        )
        val groupPurchaseJoinUser =
            groupPurchaseJoinUserRepository.findByUserIdAndGroupPurchaseId(userId, groupPurchaseId)
                ?: throw Exception("userId or groupPurchaseId not found")

        groupPurchase.groupPurchaseUsers.remove(groupPurchaseJoinUser)
        groupPurchase.userCount--

        if (groupPurchase.userCount == 0) {
            groupPurchaseRepository.delete(groupPurchase)
        } else {
            groupPurchaseRepository.save(groupPurchase)
        }

        groupPurchaseJoinUserRepository.delete(groupPurchaseJoinUser)
    }


    fun GroupPurchaseEntity.toResponse(): GroupPurchaseResponse {
        return GroupPurchaseResponse(
            id = id!!,
            productId = productId,
            userLimit = userLimit,
            userCount = userCount,
            timeLimit = timeLimit,
            discount = discount
        )
    }

    fun GroupPurchaseJoinUserEntity.toResponse(): GroupPurchaseJoinUserResponse {
        return GroupPurchaseJoinUserResponse(
            id = id!!, userId = userId, groupPurchaseId = groupPurchase.id!!
        )
    }
}
