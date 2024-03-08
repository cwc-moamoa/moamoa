package com.teamsparta.moamoa.domain.groupPurchase.service

import com.teamsparta.moamoa.domain.groupPurchase.dto.*
import com.teamsparta.moamoa.domain.groupPurchase.model.GroupPurchaseEntity
import com.teamsparta.moamoa.domain.groupPurchase.model.GroupPurchaseJoinUserEntity
import com.teamsparta.moamoa.domain.groupPurchase.repository.GroupPurchaseJoinUserRepository
import com.teamsparta.moamoa.domain.groupPurchase.repository.GroupPurchaseRepository
import com.teamsparta.moamoa.exception.ModelNotFoundException
import jakarta.transaction.Transactional
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class GroupPurchaseService(
    private val groupPurchaseRepository: GroupPurchaseRepository,
    private val groupPurchaseJoinUserRepository: GroupPurchaseJoinUserRepository,
    private val redisTemplate: RedisTemplate<String, Any>,
) {
//    @Transactional
//    fun createAndJoinOrJoinGroupPurchase(
//        request: CreateGroupPurchaseRequest, // 추후 product 완료시 정보를 자동으로 받아오게 변경
//        userId: Long, // 추후 order 완료시 정보를 자동으로 받아오게 변경
////        orderId: Long, // 추후 order 완료시 정보를 자동으로 받아오게 변경
//    ) {
//        val orderId = // 방금 끝난 주문의 orderId 를 받아옴
//        val uniqueOrderId = redisTemplate.opsForHash<String, String>().get(orderId.toString(), "productId") ?: throw Exception("redis에 정보가 없다 애송이")
//        val productId = uniqueOrderId.toLong()
//        val groupPurchase = groupPurchaseRepository.findByProductIdAndDeletedAtIsNull(productId)
//
//        if (groupPurchase == null) {
//            val newGroupPurchase =
//                GroupPurchaseEntity(
//                    productId,
//                    request.userLimit,
//                    1,
//                    request.timeLimit.plusHours(24),
//                    request.discount,
//                    mutableListOf(),
//                )
//
//            val groupPurchaseJoinUser = GroupPurchaseJoinUserEntity(userId, newGroupPurchase, order.id)
//
//            newGroupPurchase.groupPurchaseUsers.add(groupPurchaseJoinUser)
//            groupPurchaseRepository.save(newGroupPurchase)
//            redisTemplate.delete(orderId.toString())
//        } else {
//            joinGroupPurchase(userId, groupPurchase.id!!, orderId)
//        }
//    }

    fun joinGroupPurchase(
        userId: Long,
        groupPurchaseId: Long,
        orderId: Long,
    ) {
        val groupPurchase =
            groupPurchaseRepository.findByIdAndDeletedAtIsNull(groupPurchaseId) ?: throw ModelNotFoundException(
                "GroupPurchase", groupPurchaseId,
            )
        if (groupPurchase.userCount >= groupPurchase.userLimit) {
            throw IllegalStateException("GroupPurchase is full")
        }

        val existingJoinUser = groupPurchaseJoinUserRepository.findByUserIdAndGroupPurchaseId(userId, groupPurchaseId)
        if (existingJoinUser != null) {
            throw IllegalStateException("이미 신청한 공동구매 입니다.")
        }

        val groupPurchaseJoinUser = GroupPurchaseJoinUserEntity(userId, groupPurchase, orderId)
        groupPurchase.groupPurchaseUsers.add(groupPurchaseJoinUser)
        groupPurchase.userCount++

        groupPurchaseJoinUserRepository.save(groupPurchaseJoinUser)

        if (groupPurchase.userCount == groupPurchase.userLimit) {
            groupPurchase.deletedAt = LocalDateTime.now()
            groupPurchaseRepository.save(groupPurchase)

            val userSoftDelete: List<GroupPurchaseJoinUserEntity> =
                groupPurchaseJoinUserRepository.findByGroupPurchaseId(groupPurchaseId)

            userSoftDelete.forEach { user ->
                user.deletedAt = LocalDateTime.now()
            }

            groupPurchaseJoinUserRepository.saveAll(userSoftDelete)
        }
        redisTemplate.delete(orderId.toString())
    }

    @Transactional
    fun leaveGroupPurchase(
        userId: Long,
        groupPurchaseId: Long,
    ) {
        val groupPurchase =
            groupPurchaseRepository.findByIdAndDeletedAtIsNull(groupPurchaseId) ?: throw ModelNotFoundException(
                "GroupPurchase", groupPurchaseId,
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
            discount = discount,
        )
    }

    fun GroupPurchaseJoinUserEntity.toResponse(): GroupPurchaseJoinUserResponse {
        return GroupPurchaseJoinUserResponse(
            id = id!!,
            userId = userId,
            groupPurchaseId = groupPurchase.id!!,
        )
    }
}
