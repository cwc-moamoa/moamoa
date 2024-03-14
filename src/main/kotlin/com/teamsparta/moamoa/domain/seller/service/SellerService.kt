package com.teamsparta.moamoa.domain.seller.service

import com.teamsparta.moamoa.domain.seller.dto.SellerResponse
import com.teamsparta.moamoa.domain.seller.dto.SellerSignInRequest
import com.teamsparta.moamoa.domain.seller.dto.SellerSignInResponse
import com.teamsparta.moamoa.domain.seller.dto.SellerSignUpRequest

interface SellerService {
    fun signUpSeller(sellerSignUpRequest: SellerSignUpRequest): SellerResponse

    fun signInSeller(sellerSignInRequest: SellerSignInRequest): SellerSignInResponse

    fun deleteSeller(sellerId: Long): SellerResponse
}
