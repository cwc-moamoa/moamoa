package com.teamsparta.moamoa.domain.seller.service

import com.teamsparta.moamoa.domain.order.model.OrdersStatus
import com.teamsparta.moamoa.domain.order.repository.OrderRepository
import com.teamsparta.moamoa.domain.product.repository.ProductRepository
import com.teamsparta.moamoa.domain.seller.dto.*
import com.teamsparta.moamoa.domain.seller.repository.SellerRepository
import com.teamsparta.moamoa.exception.InvalidCredentialException
import com.teamsparta.moamoa.exception.ModelNotFoundException
import com.teamsparta.moamoa.infra.security.jwt.JwtPlugin
import jakarta.servlet.http.HttpServletResponse
import jdk.jshell.spi.ExecutionControlProvider
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class SellerServiceImpl(
    private val sellerRepository: SellerRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtPlugin: JwtPlugin,
    private val orderRepository: OrderRepository,
    private val productRepository: ProductRepository,
) : SellerService {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(SellerServiceImpl::class.java)
    }

    @Transactional
    override fun signUpSeller(sellerSignUpRequest: SellerSignUpRequest): SellerResponse {
        try {
            if (sellerRepository.existsByEmail(sellerSignUpRequest.email)) {
                throw IllegalStateException("이미 사용중인 이메일입니다. ")
            }
            if (sellerSignUpRequest.passwordCheck != sellerSignUpRequest.password)
                throw InvalidCredentialException("비밀번호를 확인해주세요.")
            val seller = sellerRepository.save(sellerSignUpRequest.toEntity(passwordEncoder))
            return SellerResponse.toResponse(seller)
        }
        catch (ex: Exception) {
            logger.error("판매자 회원가입 중 오류 발생", ex)
            throw ex
        }

    }

    @Transactional
    override fun signInSeller(sellerSignInRequest: SellerSignInRequest, response: HttpServletResponse): SellerSignInResponse {
        try {
            val seller =
                sellerRepository.findByEmail(sellerSignInRequest.email) ?: throw ModelNotFoundException("User", null)
            if (!passwordEncoder.matches(
                    sellerSignInRequest.password,
                    seller.password
                )
            ) throw InvalidCredentialException("비밀번호가 맞지 않습니다. ")
            return SellerSignInResponse(
                accessToken =
                jwtPlugin.generateAccessToken(
                    subject = seller.id.toString(),
                    nickname = seller.nickname,
                    email = seller.email,
                    response = response
                ),
            )
        } catch (ex: Exception) {
            logger.error("판매자 로그인 중 오류 발생", ex)
            throw ex
        }
    }

    @Transactional
    override fun deleteSeller(sellerId: Long): SellerResponse {
        try {
            val seller = sellerRepository.findByIdAndDeletedAtIsNull(sellerId)
                .orElseThrow { ModelNotFoundException("Seller", sellerId) }

            if (sellerId != seller.id) {
                throw InvalidCredentialException()
            }
            val foundOrders = orderRepository.findBySellerIdAndDeletedAtIsNull(sellerId)
            if (foundOrders.any { it.status != OrdersStatus.DELIVERED && it.status != OrdersStatus.CANCELLED }) {
                throw Exception("처리되지 않은 주문이 존재합니다. ")
            }

            val foundProduct = productRepository.findBySellerIdAndDeletedAtIsNull(sellerId)
            if (foundProduct.any { it.seller.id == sellerId }) {
                throw Exception("게시된 판매상품이 존재합니다. ")
            }
            seller.deletedAt = LocalDateTime.now()
            sellerRepository.save(seller)

            return SellerResponse.toResponse(seller)
        } catch (ex: Exception) {
            logger.error("판매자 회원 탈퇴 중 오류 발생", ex)
            throw ex
        }

    }
}
