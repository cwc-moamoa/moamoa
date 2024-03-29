package com.teamsparta.moamoa.domain.review.dto

import com.teamsparta.moamoa.domain.order.model.OrdersEntity
import com.teamsparta.moamoa.domain.product.model.Product
import com.teamsparta.moamoa.domain.review.model.Review
import com.teamsparta.moamoa.domain.socialUser.model.SocialUser
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Size

data class CreateReviewRequest(
    @field:Size(min = 2, max = 10, message = "제목은 2자 이상 10자 이하 작성해주세요.")
    val title: String,
    @field:Size(min = 10, max = 300, message = "내용은 10자 이상 300자 이하 작성해주세요.")
    val content: String,
    val imageUrl: String?,
    @field:[Min(value = 1, message = "1부터 5사이의 별점을 입력해주세요.") Max(value = 5, message = "1부터 5사이의 별점을 입력해주세요.")] val rating: Int,
) {
    fun toReview(
        product: Product,
        socialUser: SocialUser,
        order: OrdersEntity,
    ): Review {
        return Review(
            title = title,
            content = content,
            imageUrl = imageUrl,
            product = product,
            likes = 0,
            rating = rating,
            socialUser = socialUser,
            order = order,
        )
    }
}
