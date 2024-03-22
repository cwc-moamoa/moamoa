package com.teamsparta.moamoa.domain.product.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size

data class ProductRequest(

    @Schema(description = "제목은 최대 250자까지 가능하며, 최소 1자 이상이어야 합니다", example = "사과")
    @field:Size(min = 1, max = 250, message = "제목은 최소 1자 이상, 최대 250자까지 가능합니다.")
    val title: String,

    @Schema(description = "내용은 최소 1자 이상, 최대 1000자까지 가능합니다", example = "상품 설명")
    @field:Size(min = 1 ,max = 1000, message = "내용은 최소 1자 이상, 최대 1000자까지 가능합니다.")
    val content: String,
    val imageUrl: String,

    @Schema(description = "가격은 0보다 커야 합니다", example = "100")
    @field:Positive(message = "가격은 0보다 커야 합니다.")
    val price: Double,

    val purchaseAble: Boolean,
    val userLimit: Int,
    val discount: Double,

    @Schema(description = "재고는 0보다 커야 합니다", example = "10")
    @field:Positive(message = "재고는 0보다 커야 합니다.")
    val stock: Int,
)
