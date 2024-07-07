package com.teamsparta.moamoa.infra.redis

interface RedissonLockManager {

    fun acquireLock(lockKey: String, waitTime: Long, leaseTime: Long,):Boolean

    fun releaseLock(lockKey: String)


}