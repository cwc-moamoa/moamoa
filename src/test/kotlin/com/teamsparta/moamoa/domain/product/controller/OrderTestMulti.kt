package com.teamsparta.moamoa.domain.product.controller

import com.teamsparta.moamoa.domain.groupPurchase.repository.GroupPurchaseJoinUserRepository
import com.teamsparta.moamoa.domain.groupPurchase.repository.GroupPurchaseRepository
import com.teamsparta.moamoa.domain.order.repository.OrderRepository
import com.teamsparta.moamoa.domain.order.service.OrderServiceImpl
import com.teamsparta.moamoa.domain.payment.repository.PaymentRepository
import com.teamsparta.moamoa.domain.product.model.Product
import com.teamsparta.moamoa.domain.product.model.ProductStock
import com.teamsparta.moamoa.domain.product.repository.ProductRepository
import com.teamsparta.moamoa.domain.product.repository.ProductStockRepository
import com.teamsparta.moamoa.domain.seller.model.Seller
import com.teamsparta.moamoa.domain.seller.repository.SellerRepository
import com.teamsparta.moamoa.domain.socialUser.model.OAuth2Provider
import com.teamsparta.moamoa.domain.socialUser.model.SocialUser
import com.teamsparta.moamoa.domain.socialUser.repository.SocialUserRepository
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.test.context.ActiveProfiles
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderTestMulti
@Autowired constructor(
    private val orderRepository: OrderRepository,
    private val productRepository: ProductRepository,
    private val productStockRepository: ProductStockRepository,
    private val sellerRepository: SellerRepository,
    private val redisConfigTemplate: RedisTemplate<String, Any>,
    private val paymentRepository: PaymentRepository,
    private val groupPurchaseRepository: GroupPurchaseRepository,
    private val socialUserRepository: SocialUserRepository,
    private val groupPurchaseJoinUserRepository: GroupPurchaseJoinUserRepository,
) {
    private val orderService = OrderServiceImpl(
        orderRepository, productRepository, productStockRepository, sellerRepository,
        redisConfigTemplate,
        paymentRepository, groupPurchaseRepository, socialUserRepository, groupPurchaseJoinUserRepository
    )

    @Test
    fun `여러 사용자가 동시에 주문을 할시 테스트`() {
        // GIVEN
        val executorService: ExecutorService = Executors.newFixedThreadPool(5)

        val latch = CountDownLatch(5)


        // WHEN
        repeat(15) {
            executorService.execute {

                val user = SocialUser(
                    nickname = "nick$it", email = "zzz$it", provider = OAuth2Provider.KAKAO, providerId = "123$it"
                )
                socialUserRepository.save(user)

                val seller = Seller(
                    nickname = "seller$it",
                    email = "seller$it@example.com",
                    address = "123 Street",
                    bizRegistrationNumber = "123456789$it",
                    password = "password$it",
                    phoneNumber = "123-456-$it"
                )
                sellerRepository.save(seller)

                val product = Product(
                    content = "123$it",
                    discount = 0.0,
                    imageUrl = "string$it",
                    likes = 0,
                    price = 200.0,
                    purchaseAble = false,
                    seller = seller,
                    title = "yyy$it",
                    userLimit = 4
                )
                productRepository.save(product)

                val productStock = ProductStock(
                    product = product, stock = 1000, productName = product.title
                )
                productStockRepository.save(productStock)


                orderService.createOrder(
                        userId = user.id!!,
                        productId = product.id!!,
                        quantity = 1,
                        address = "Address",
                        phoneNumber = "123-456-7890",
                    )
                    latch.countDown()
            }
        }

        latch.await()

        executorService.shutdown()

        // THEN
        val orders = orderService.getAllOrders()
        orders.size shouldBe 15
    }
}

