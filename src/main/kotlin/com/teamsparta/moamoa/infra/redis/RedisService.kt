package com.teamsparta.moamoa.infra.redis

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

@Component
class RedisService(
    private val redisTemplate: RedisTemplate<String, Any>,
) {
    fun saveToRedis(
        productId: String,
        userId: String,
        orderId: String,
    )  {
        val hashKey: String = orderId

        redisTemplate.opsForHash<String, String>().put(hashKey, "productId", productId)
        redisTemplate.opsForHash<String, String>().put(hashKey, "userId", userId)
        redisTemplate.opsForHash<String, String>().put(hashKey, "orderId", orderId)
    }
}
