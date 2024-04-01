//package com.teamsparta.moamoa.domain.product.controller
//
//import com.teamsparta.moamoa.domain.groupPurchase.repository.GroupPurchaseJoinUserRepository
//import com.teamsparta.moamoa.domain.groupPurchase.repository.GroupPurchaseRepository
//import com.teamsparta.moamoa.domain.order.repository.OrderRepository
//import com.teamsparta.moamoa.domain.order.service.OrderServiceImpl
//import com.teamsparta.moamoa.domain.payment.repository.PaymentRepository
//import com.teamsparta.moamoa.domain.product.repository.ProductRepository
//import com.teamsparta.moamoa.domain.product.repository.ProductStockRepository
//import com.teamsparta.moamoa.domain.seller.repository.SellerRepository
//import com.teamsparta.moamoa.domain.socialUser.model.OAuth2Provider
//import com.teamsparta.moamoa.domain.socialUser.model.SocialUser
//import com.teamsparta.moamoa.domain.socialUser.repository.SocialUserRepository
//import com.teamsparta.moamoa.infra.redis.RedissonLockManager
//import io.kotest.matchers.shouldBe
//import org.junit.jupiter.api.Test
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
//import org.springframework.boot.test.context.SpringBootTest
//import java.util.concurrent.CountDownLatch
//import java.util.concurrent.ExecutorService
//import java.util.concurrent.Executors
//
//@SpringBootTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//class OrderTestMultiWithLock
//    @Autowired
//    constructor(
//        private val orderRepository: OrderRepository,
//        private val productRepository: ProductRepository,
//        private val productStockRepository: ProductStockRepository,
//        private val sellerRepository: SellerRepository,
//        private val paymentRepository: PaymentRepository,
//        private val groupPurchaseRepository: GroupPurchaseRepository,
//        private val socialUserRepository: SocialUserRepository,
//        private val groupPurchaseJoinUserRepository: GroupPurchaseJoinUserRepository,
//        private val redissonLockManager: RedissonLockManager,
//    ) {
//        private val orderService =
//            OrderServiceImpl(
//                orderRepository,
//                productRepository,
//                productStockRepository,
//                sellerRepository,
//                paymentRepository,
//                groupPurchaseRepository,
//                socialUserRepository,
//                groupPurchaseJoinUserRepository,
//                redissonLockManager,
//            )
//
//        @Test
//        fun `락이 있을 경우 여러 사용자가 동시에 한 상품을 주문하는 테스트`() {
//            // GIVEN
//            val executorService: ExecutorService = Executors.newFixedThreadPool(5)
//            val latch = CountDownLatch(5)
//
//            // WHEN
//            repeat(5) {
//                executorService.execute {
//                    val user =
//                        SocialUser(
//                            nickname = "nick$it",
//                            email = "zzz$it",
//                            provider = OAuth2Provider.KAKAO,
//                            providerId = "123$it",
//                        )
//                    socialUserRepository.save(user)
//
//                    orderService.createOrderWithLock(
//                        userId = user.id!!,
//                        productId = 2,
//                        quantity = 1,
//                        address = "Address",
//                        phoneNumber = "123-456-7890",
//                    )
//
//                    latch.countDown()
//                }
//            }
//
//            latch.await()
//            executorService.shutdown()
//
//            // THEN
//            val findProduct = productRepository.findByIdAndDeletedAtIsNull(2).orElseThrow { Exception("존재하지 않는 상품입니다") }
//            val stockCheck = productStockRepository.findByProduct(findProduct)
//
//            stockCheck!!.stock shouldBe 3530
//        }
//    }
