package com.teamsparta.moamoa.infra.redis

interface RedisService {

    fun saveToRedis(
        productId: String,
        userId: String,
        orderId: String,
    )

}