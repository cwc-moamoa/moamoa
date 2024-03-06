package com.teamsparta.moamoa.domain.product.repository

import com.teamsparta.moamoa.domain.product.model.ProductEntity
import com.teamsparta.moamoa.domain.seller.model.SellerEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository : JpaRepository<ProductEntity, Long>{
    fun findBySeller(findSeller:SellerEntity):List<ProductEntity?>
}
