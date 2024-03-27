package com.teamsparta.moamoa.infra.redis

import org.redisson.api.RLock
import org.redisson.api.RedissonClient
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class RedissonLockManager(private val redissonClient: RedissonClient) {
    private val log = LoggerFactory.getLogger(RedissonLockManager::class.java)

    fun acquireLock(
        lockKey: String,
        waitTime: Long,
        leaseTime: Long,
    ): Boolean {
        val lock: RLock = redissonClient.getLock(lockKey)

        try {
            val lockable = lock.tryLock(waitTime, leaseTime, TimeUnit.MILLISECONDS)
            if (!lockable) {
                log.info("Lock 획득 실패={}", lockKey)
                return false
            }
            log.info("로직 수행")
            return true
        } catch (e: InterruptedException) {
            log.info("에러 발생")
            throw e
        }
    }

    fun releaseLock(lockKey: String) {
        val lock: RLock = redissonClient.getLock(lockKey)
        log.info("락 해제")
        lock.unlock()
    }
}
