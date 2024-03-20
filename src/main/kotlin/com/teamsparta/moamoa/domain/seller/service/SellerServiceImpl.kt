package com.teamsparta.moamoa.domain.seller.service

import com.teamsparta.moamoa.domain.order.repository.OrderRepository
import com.teamsparta.moamoa.domain.seller.dto.*
import com.teamsparta.moamoa.domain.seller.repository.SellerRepository
import com.teamsparta.moamoa.exception.InvalidCredentialException
import com.teamsparta.moamoa.exception.ModelNotFoundException
import com.teamsparta.moamoa.infra.security.jwt.JwtPlugin
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class SellerServiceImpl(
    private val sellerRepository: SellerRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtPlugin: JwtPlugin,
    private val orderRepository: OrderRepository
) : SellerService {
    @Transactional
    override fun signUpSeller(sellerSignUpRequest: SellerSignUpRequest): SellerResponse {
        if (sellerRepository.existsByEmail(sellerSignUpRequest.email)) {
            throw IllegalStateException("Email is already in use")
        }
        if (sellerSignUpRequest.passwordCheck != sellerSignUpRequest.password) throw InvalidCredentialException()
        val seller = sellerRepository.save(sellerSignUpRequest.toEntity(passwordEncoder))
        return SellerResponse.toResponse(seller)
    }

    @Transactional
    override fun signInSeller(sellerSignInRequest: SellerSignInRequest): SellerSignInResponse {
        val seller = sellerRepository.findByEmail(sellerSignInRequest.email) ?: throw ModelNotFoundException("User", null)
        if (!passwordEncoder.matches(sellerSignInRequest.password, seller.password)) throw InvalidCredentialException()
        return SellerSignInResponse(
            accessToken =
                jwtPlugin.generateAccessToken(
                    subject = seller.id.toString(),
                    nickname = seller.nickname,
                    email = seller.email,
                ),
        )
    }

    override fun deleteSeller(sellerId: Long): SellerResponse {
        val seller = sellerRepository.findByIdAndDeletedAtIsNull(sellerId).orElseThrow { ModelNotFoundException("Seller", sellerId) }

        if (sellerId != seller.id) {
            throw InvalidCredentialException()
        }

        seller.deletedAt = LocalDateTime.now()
        sellerRepository.save(seller)

        return SellerResponse.toResponse(seller)
    }
}
