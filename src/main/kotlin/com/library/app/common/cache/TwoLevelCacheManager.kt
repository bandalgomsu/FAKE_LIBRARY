package com.library.app.common.cache

import com.library.app.common.redis.RedisClient
import com.library.app.common.redis.RedisConnectContext
import com.library.app.common.redis.RedisTopic
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.stereotype.Component


@Component
class TwoLevelCacheManager(
    private val caffeineCacheManager: CaffeineCacheManager,
    private val redisClient: RedisClient,
    private val redisConnectContext: RedisConnectContext,
) {

    suspend fun <T : Any> getOrPut(
        cacheType: CacheType,
        key: String,
        type: Class<T>,
        loader: suspend () -> T
    ): T {
        if (redisConnectContext.isConnect) {
            val cacheKey = createCacheKey(cacheType, key)

            val localCache = caffeineCacheManager.getCache(cacheType.cacheName)

            localCache?.get(key)?.let {
                return it.get() as T
            }

            val redisValue = redisClient.getData(cacheKey, type)
            if (redisValue != null) {
                localCache?.put(key, redisValue) // 로컬 캐시에 저장
                return redisValue
            }
            val value = loader()
            localCache?.put(key, value)
            redisClient.setData(cacheKey, value, cacheType.redisExpireSeconds)

            return value
        }

        return loader()
    }

    suspend fun <T : Any> evict(
        cacheType: CacheType,
        key: String,
        type: Class<T>,
        loader: suspend () -> T
    ): T {
        if (redisConnectContext.isConnect) {
            redisClient.deleteData(key)

            redisClient.publish(RedisTopic.CACHE_EVICT, createCacheKey(cacheType, key))
        }

        return loader()
    }

    private fun createCacheKey(cacheType: CacheType, key: String): String {
        return "${cacheType.cacheName}-$key"
    }
}