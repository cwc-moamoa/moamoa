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

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderTest
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
    fun `한명의 사용자가 주문을 성공적으로 하나 테스트`() {
        // GIVEN
        val someSocialUser = SocialUser(
            id = 1, nickname = "nick", email = "zzz", provider = OAuth2Provider.KAKAO, providerId = "123"
        )
        socialUserRepository.save(someSocialUser)
        val someSeller = Seller(
            nickname = "nick",
            email = "zzz",
            address = "135",
            bizRegistrationNumber = "135136416",
            password = "16146146",
            phoneNumber = "0000"
        )
        sellerRepository.save(someSeller)
        val someProduct = Product(
            id = 1L,
            content = "123",
            discount = 0.0,
            imageUrl = "string",
            likes = 0,
            price = 200.0,
            purchaseAble = false,
            seller = someSeller,
            title = "yyy",
            userLimit = 4,
        )
        val someProductStock = ProductStock(
            id = 1L, product = someProduct, stock = 1000, productName = someProduct.title
        )
        productRepository.save(someProduct)
        productStockRepository.save(someProductStock)

        // When
        orderService.createOrder(
            userId = someSocialUser.id!!,
            productId = someProduct.id!!,
            quantity = 5,
            address = "Address",
            phoneNumber = "123-456-7890",
        )

        // THEN
        val orders = orderService.getAllOrders()
        orders.size shouldBe 1
    }
}