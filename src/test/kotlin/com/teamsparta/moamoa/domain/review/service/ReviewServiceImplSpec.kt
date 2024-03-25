package com.teamsparta.moamoa.domain.review.service

import com.teamsparta.moamoa.domain.fixture.CreateOrdersEntityFixture
import com.teamsparta.moamoa.domain.fixture.CreateProductFixture
import com.teamsparta.moamoa.domain.fixture.CreateReviewFixture
import com.teamsparta.moamoa.domain.fixture.CreateSocialUserFixture
import com.teamsparta.moamoa.domain.order.repository.OrderRepository
import com.teamsparta.moamoa.domain.product.repository.ProductRepository
import com.teamsparta.moamoa.domain.review.dto.CreateReviewRequest
import com.teamsparta.moamoa.domain.review.repository.ReviewRepository
import com.teamsparta.moamoa.domain.socialUser.repository.SocialUserRepository
import com.teamsparta.moamoa.infra.security.UserPrincipal
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.util.Optional

class ReviewServiceImplSpec: BehaviorSpec() {
    init {
        given("ReviewServiceImpl") {
            val productId = 1L
            val socialUser = UserPrincipal(
                id = 1L,
                email = "userEmail"
            )
            val ReviewResponse = CreateReviewRequest(
                title = "title",
                content = "content",
                imageUrl = "imageUrl",
                rating = 5
            )


            `when`("createReview") {
                val reviewRepository = mockk<ReviewRepository>()
                every { reviewRepository.save(any()) } returns CreateReviewFixture.createReview(
                    ReviewResponse.title,
                    ReviewResponse.content,
                    ReviewResponse.rating,
                    ReviewResponse.imageUrl!!
                )

                val productRepository = mockk<ProductRepository>()
                every { productRepository.findByIdAndDeletedAtIsNull(any()) } returns Optional.of(CreateProductFixture.createProduct())

                val socialUserRepository = mockk<SocialUserRepository>()
                every { socialUserRepository.findByEmail(any()) } returns CreateSocialUserFixture.createSocialUser()

                val orderRepository = mockk<OrderRepository>()
                every { orderRepository.findByProductIdAndSocialUserId(any(), any()) } returns Optional.of(CreateOrdersEntityFixture.createOrder())

                val reviewServiceImpl = ReviewServiceImpl(
                    reviewRepository = reviewRepository,
                    productRepository = productRepository,
                    socialUserRepository = socialUserRepository,
                    orderRepository = orderRepository,
                )

                val result = reviewServiceImpl.createReview(
                    productId = productId,
                    socialUser = socialUser,
                    createReviewRequest = ReviewResponse
                )

                then("should return ReviewResponse") {
                    result.title shouldBe ReviewResponse.title
                    result.content shouldBe ReviewResponse.content
                    result.imageUrl shouldBe ReviewResponse.imageUrl
                    result.rating shouldBe ReviewResponse.rating
                }
            }
        }
    }
}