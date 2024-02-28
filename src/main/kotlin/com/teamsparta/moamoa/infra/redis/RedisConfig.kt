package com.teamsparta.moamoa.infra.redis

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.StringRedisSerializer

//@Configuration
//class RedisConfig() {
//    @Value("\${spring.data.redis.port}")
//    private val port = 0
//
//    @Value("\${spring.data.redis.host}")
//    private val host: String = ""
//
//    @Bean
//    fun redisConnectionFactory(): RedisConnectionFactory {
//        return LettuceConnectionFactory(host, port)
//    }
//
//    @Bean
//    fun redisTemplate(): RedisTemplate<String, Any> {
//        return RedisTemplate<String, Any>().apply {
//            this.connectionFactory = redisConnectionFactory()
//
//            this.keySerializer = StringRedisSerializer()
//            this.valueSerializer = StringRedisSerializer()
//
//            this.hashKeySerializer = StringRedisSerializer()
//            this.hashValueSerializer = StringRedisSerializer()
//        }
//    }
//}

// Redis 사용시 활성화