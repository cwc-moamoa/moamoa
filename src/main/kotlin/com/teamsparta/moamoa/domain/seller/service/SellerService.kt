package com.teamsparta.moamoa.domain.seller.service

import com.teamsparta.moamoa.domain.seller.dto.SellerResponse
import com.teamsparta.moamoa.domain.seller.dto.SellerSignInRequest
import com.teamsparta.moamoa.domain.seller.dto.SellerSignInResponse
import com.teamsparta.moamoa.domain.seller.dto.SellerSignUpRequest
import jakarta.servlet.http.HttpServletResponse

interface SellerService {
    fun signUpSeller(sellerSignUpRequest: SellerSignUpRequest): SellerResponse

    fun signInSeller(sellerSignInRequest: SellerSignInRequest, response: HttpServletResponse): SellerSignInResponse

    fun deleteSeller(sellerId: Long): SellerResponse
}
