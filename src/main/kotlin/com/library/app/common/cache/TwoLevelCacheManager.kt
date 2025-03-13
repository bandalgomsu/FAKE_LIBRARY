package com.library.app.common.cache

import com.library.app.common.redis.RedisClient
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.stereotype.Component


@Component
class TwoLevelCacheManager(
    private val caffeineCacheManager: CaffeineCacheManager,
    private val redisClient: RedisClient
) {

    suspend fun <T : Any> getOrLoad(
        cacheName: String,
        key: String,
        type: Class<T>,
        redisExpireSeconds: Long = 60 * 60,
        loader: suspend () -> T
    ): T {
        val localCache = caffeineCacheManager.getCache(cacheName)

        localCache?.get(key)?.let {
            return it.get() as T
        }

        val redisValue = redisClient.getData("$cacheName:$key", type)
        if (redisValue != null) {
            localCache?.put(key, redisValue) // 로컬 캐시에 저장
            return redisValue
        }

        val value = loader()
        localCache?.put(key, value)
        redisClient.setData("$cacheName:$key", value, redisExpireSeconds)

        return value
    }

    suspend fun <T : Any> evict(
        cacheName: String,
        key: String,
        type: Class<T>,
        redisExpireSeconds: Long = 60 * 60,
        loader: suspend () -> T
    ): T {
        val localCache = caffeineCacheManager.getCache(cacheName)
        localCache?.evict(key)

        redisClient.deleteData("$cacheName:$key")
        
        return loader()
    }
}