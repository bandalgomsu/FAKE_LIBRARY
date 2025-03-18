package com.library.app.common.cache

import com.library.app.common.redis.RedisClient
import org.springframework.stereotype.Component

@Component("distributeRedisCacheManager")
class DistributeRedisCacheManager(
    private val redisClient: RedisClient
) : CacheManager {

    override suspend fun <T : Any> getOrLoad(
        cacheType: CacheType,
        key: String,
        type: Class<T>,
        loader: suspend () -> T
    ): T {
        val cacheKey = cacheType.createCacheKey(key)

        val redisValue = redisClient.getData(cacheKey, type)
        if (redisValue != null) {
            return redisValue
        }
        val value = loader()

        redisClient.setData(cacheKey, value, cacheType.redisExpireSeconds)

        return value
    }

    override suspend fun <T : Any> evict(
        cacheType: CacheType,
        key: String,
        type: Class<T>,
        loader: suspend () -> T
    ): T {
        val cacheKey = cacheType.createCacheKey(key)

        val value = loader()

        redisClient.deleteData(cacheKey)

        return value
    }

}