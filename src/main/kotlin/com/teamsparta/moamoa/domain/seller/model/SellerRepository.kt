package com.teamsparta.moamoa.domain.seller.model

import org.springframework.data.jpa.repository.JpaRepository

interface SellerRepository:JpaRepository<SellerEntity,Long> {
}