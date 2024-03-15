package com.teamsparta.moamoa.domain.seller.dto

import com.teamsparta.moamoa.domain.seller.model.Seller

data class SellerResponse(
    val id: Long,
    val email: String,
    val nickname: String,
    var address: String,
    var phoneNumber: String,
    var bizRegistrationNumber: String,
) {
    companion object {
        fun toResponse(seller: Seller): SellerResponse {
            return SellerResponse(
                seller.id!!,
                seller.email,
                seller.nickname,
                seller.address,
                seller.phoneNumber,
                seller.bizRegistrationNumber,
            )
        }
    }
}
