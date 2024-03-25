package com.teamsparta.moamoa.domain.fixture

import com.teamsparta.moamoa.domain.product.model.Product
import com.teamsparta.moamoa.domain.seller.model.Seller

class CreateProductFixture {
    companion object {
        fun createProduct() : Product {
            return Product(
                id = 1L,
                seller = Seller(
                    email = "email",
                    password = "password",
                    nickname = "nickname",
                    address = "address",
                    phoneNumber = "phoneNumber",
                    bizRegistrationNumber = "bizRegistrationNumber"
                ),
                price = 10000.0,
                title = "title",
                content = "content",
                purchaseAble = true,
                imageUrl = "imageUrl",
                likes = 0,
                userLimit = 100,
                discount = 0.0,
                isSoldOut = false
            )
        }
    }
}