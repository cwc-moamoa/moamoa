package com.teamsparta.moamoa.domain.groupPurchase.service

import com.teamsparta.moamoa.domain.groupPurchase.repository.GroupPurchaseJoinUserRepository
import com.teamsparta.moamoa.domain.groupPurchase.repository.GroupPurchaseRepository
import org.springframework.stereotype.Service
import com.teamsparta.moamoa.domain.groupPurchase.dto.*
import com.teamsparta.moamoa.domain.groupPurchase.model.GroupPurchaseEntity
import com.teamsparta.moamoa.domain.groupPurchase.model.GroupPurchaseJoinUserEntity
import com.teamsparta.moamoa.exception.ModelNotFoundException
import jakarta.transaction.Transactional
import java.time.LocalDateTime

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

//    @Transactional
//    fun createAndJoinGroupPurchase(request: CreateGroupPurchaseRequest , userId: Long ): GroupPurchaseResponse {
//        val groupPurchase = GroupPurchaseEntity(
//            request.productId, request.userLimit, 1, request.timeLimit, request.discount, mutableListOf()
//        )
//
//        val groupPurchaseJoinUser = GroupPurchaseJoinUserEntity(userId, groupPurchase)
//
//        groupPurchase.groupPurchaseUsers.add(groupPurchaseJoinUser)
//
//        val savedGroupPurchase = groupPurchaseRepository.save(groupPurchase)
//
//        return savedGroupPurchase.toResponse()
//    }

    //    if 프로덕트id에 해당되는 groupPurchase가 없으면 방을 새로 만든다
    //    있을시 else로 joinGroupPurchase 실행?

    @Transactional
    fun createAndJoinOrJoinGroupPurchase(request: CreateGroupPurchaseRequest, userId: Long) {
        val groupPurchase = groupPurchaseRepository.findByProductId(request.productId)

        if (groupPurchase == null) {
            val newGroupPurchase = GroupPurchaseEntity(
                request.productId, request.userLimit, 1, request.timeLimit.plusHours(24), request.discount, mutableListOf()
            )

            val groupPurchaseJoinUser = GroupPurchaseJoinUserEntity(userId, newGroupPurchase)

            newGroupPurchase.groupPurchaseUsers.add(groupPurchaseJoinUser)
            groupPurchaseRepository.save(newGroupPurchase)
        } else {
            joinGroupPurchase(userId, groupPurchase.id!!)
        }
    }



    @Transactional
    fun joinGroupPurchase(userId: Long, groupPurchaseId: Long) {
        val groupPurchase = groupPurchaseRepository.findByIdOrNull(groupPurchaseId) ?: throw ModelNotFoundException(
            "GroupPurchase", groupPurchaseId
        )
        if (groupPurchase.userCount >= groupPurchase.userLimit) {
            throw IllegalStateException("GroupPurchase is full")
        }

        val existingJoinUser = groupPurchaseJoinUserRepository.findByUserIdAndGroupPurchaseId(userId, groupPurchaseId)
        if (existingJoinUser != null) {
            throw IllegalStateException("이미 신청한 공동구매 입니다.")
        }

        val groupPurchaseJoinUser = GroupPurchaseJoinUserEntity(userId, groupPurchase)
        groupPurchase.groupPurchaseUsers.add(groupPurchaseJoinUser)
        groupPurchase.userCount++

        groupPurchaseJoinUserRepository.save(groupPurchaseJoinUser)

        if (groupPurchase.userCount == groupPurchase.userLimit) {
            groupPurchase.deletedAt = LocalDateTime.now()
            groupPurchaseRepository.save(groupPurchase)

            val userSoftDelete: List<GroupPurchaseJoinUserEntity> = groupPurchaseJoinUserRepository.findByGroupPurchaseId(groupPurchaseId)

            userSoftDelete.forEach { user ->
                user.deletedAt = LocalDateTime.now()
            }

            groupPurchaseJoinUserRepository.saveAll(userSoftDelete)
        }
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
            groupPurchase.deletedAt = LocalDateTime.now()
            groupPurchaseRepository.save(groupPurchase)
        }

        groupPurchaseJoinUser.deletedAt = LocalDateTime.now()
        groupPurchaseJoinUserRepository.save(groupPurchaseJoinUser)
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
