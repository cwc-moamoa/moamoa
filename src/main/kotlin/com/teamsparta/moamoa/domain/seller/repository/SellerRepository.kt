package com.teamsparta.moamoa.domain.seller.repository

import com.teamsparta.moamoa.domain.seller.model.SellerEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SellerRepository : JpaRepository<SellerEntity, Long>
