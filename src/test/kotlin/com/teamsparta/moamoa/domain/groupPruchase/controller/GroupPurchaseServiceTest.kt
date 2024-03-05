package com.teamsparta.moamoa.domain.groupPruchase.controller

import com.teamsparta.moamoa.domain.groupPurchase.dto.CreateGroupPurchaseRequest
import com.teamsparta.moamoa.domain.groupPurchase.repository.GroupPurchaseJoinUserRepository
import com.teamsparta.moamoa.domain.groupPurchase.repository.GroupPurchaseRepository
import com.teamsparta.moamoa.domain.groupPurchase.service.GroupPurchaseService
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class GroupPurchaseServiceTest
    @Autowired
    constructor(
        private val groupPurchaseRepository: GroupPurchaseRepository,
        private val groupPurchaseJoinUserRepository: GroupPurchaseJoinUserRepository,
    ) {
        private val groupPurchaseService = GroupPurchaseService(groupPurchaseRepository, groupPurchaseJoinUserRepository)

        @Test
        fun `동시에 여러 사용자가 공동구매에 참여하는 테스트`() {
            // GIVEN
            val request =
                CreateGroupPurchaseRequest(
                    productId = 1L,
                    userLimit = 2,
                    timeLimit = LocalDateTime.now().plusDays(3),
                    discount = 0.2,
                )

            // WHEN
            runBlocking {
                val groupPurchase = groupPurchaseRepository.findByProductId(request.productId)
                val jobs =
                    (1..51).map { userId ->
                        launch {
                            if (groupPurchase == null) {
                                groupPurchaseService.createAndJoinOrJoinGroupPurchase(request, userId.toLong())
                            } else {
                                groupPurchaseService.joinGroupPurchase(userId.toLong(), groupPurchase.id!!)
                            }
                        }
                    }
                jobs.forEach { it.join() }
            }

            // THEN
            val groupPurchase = groupPurchaseRepository.findByProductId(request.productId)
            groupPurchase!!.userCount shouldBe 1
        }
    }
