package com.teamsparta.moamoa.domain.groupPurchase.service

import com.teamsparta.moamoa.domain.groupPurchase.model.GroupPurchaseEntity
import com.teamsparta.moamoa.domain.groupPurchase.model.GroupPurchaseJoinUserEntity
import com.teamsparta.moamoa.domain.groupPurchase.repository.GroupPurchaseJoinUserRepository
import com.teamsparta.moamoa.domain.groupPurchase.repository.GroupPurchaseRepository
import com.teamsparta.moamoa.domain.product.repository.ProductRepository
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
    private val productRepository: ProductRepository,
) {
    @Transactional
    fun createAndJoinOrJoinGroupPurchase(
        userId: String,
        orderId: String,
    ) {
        val uniqueOrderId = redisTemplate.opsForHash<String, String>().get(orderId, "productId") ?: throw Exception("redis에 정보가 없습니다")
        val productId = uniqueOrderId.toLong()
        val groupPurchase = groupPurchaseRepository.findByProductIdAndDeletedAtIsNull(productId)
        val findProduct =
            productRepository.findById(productId)
                .orElseThrow { ModelNotFoundException("product", productId) }

        if (groupPurchase == null) {
            val newGroupPurchase =
                GroupPurchaseEntity(
                    productId,
                    findProduct.userLimit,
                    1,
                    timeLimit = LocalDateTime.now().plusHours(24),
                    mutableListOf(),
                )

            val groupPurchaseJoinUser = GroupPurchaseJoinUserEntity(userId.toLong(), newGroupPurchase, uniqueOrderId.toLong())

            newGroupPurchase.groupPurchaseUsers.add(groupPurchaseJoinUser)
            groupPurchaseRepository.save(newGroupPurchase)
            redisTemplate.delete(orderId)
        } else {
            joinGroupPurchase(userId.toLong(), groupPurchase.id!!, orderId.toLong())
        }
    }

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
            throw Exception("GroupPurchase is full")
        }

        val existingJoinUser = groupPurchaseJoinUserRepository.findByUserIdAndGroupPurchaseId(userId, groupPurchaseId)
        if (existingJoinUser != null) {
            throw Exception("이미 신청한 공동구매 입니다.")
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

        groupPurchase.groupPurchaseUsers.remove(groupPurchaseJoinUser)//이건 나도 해야댐
        groupPurchase.userCount--

        if (groupPurchase.userCount == 0) {
            groupPurchase.deletedAt = LocalDateTime.now()
            groupPurchaseRepository.save(groupPurchase)
        }

        groupPurchaseJoinUser.deletedAt = LocalDateTime.now()
        groupPurchaseJoinUserRepository.save(groupPurchaseJoinUser)
    }


//    fun GroupPurchaseEntity.toResponse(): GroupPurchaseResponse {
//        return GroupPurchaseResponse(
//            id = id!!,
//            productId = productId,
//            userLimit = userLimit,
//            userCount = userCount,
//            timeLimit = timeLimit,
//
//        )
//    }
//
//    fun GroupPurchaseJoinUserEntity.toResponse(): GroupPurchaseJoinUserResponse {
//        return GroupPurchaseJoinUserResponse(
//            id = id!!,
//            userId = userId,
//            groupPurchaseId = groupPurchase.id!!,
//        )
//    }
}
