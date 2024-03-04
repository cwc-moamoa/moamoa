package com.teamsparta.moamoa.infra.redis

// @Configuration
// class RedisConfig() {
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
// }

// Redis 사용시 활성화
