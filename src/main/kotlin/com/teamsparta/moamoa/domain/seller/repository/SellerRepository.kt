package com.teamsparta.moamoa.domain.seller.repository

import com.teamsparta.moamoa.domain.seller.model.Seller
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface SellerRepository : JpaRepository<Seller, Long> {
    fun existsByEmail(email: String): Boolean

    fun findByEmail(email: String): Seller?

    fun findByIdAndDeletedAtIsNull(sellerId: Long): Optional<Seller>
}
