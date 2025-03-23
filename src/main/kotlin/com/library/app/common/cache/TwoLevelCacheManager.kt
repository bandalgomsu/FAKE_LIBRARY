package com.library.app.common.cache

import com.library.app.common.redis.RedisClient
import com.library.app.common.redis.RedisConnectContext
import com.library.app.common.redis.RedisTopic
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component


@Component
class TwoLevelCacheManager(
    @Qualifier("localCaffeineCacheManager") private val localCacheManager: CacheManager,
    @Qualifier("distributeRedisCacheManager") private val distributeCacheManager: CacheManager,

    private val redisClient: RedisClient,
    private val redisConnectContext: RedisConnectContext,
) : CacheManager {

    override suspend fun <T : Any> getOrLoad(
        cacheType: CacheType,
        key: String,
        type: Class<T>,
        loader: suspend () -> T
    ): T {
        if (redisConnectContext.isConnect) {
            return localCacheManager.getOrLoad(cacheType, key, type) {
                distributeCacheManager.getOrLoad(cacheType, key, type) { loader() }
            }
        }

        return loader()
    }

    override suspend fun <T : Any> evict(
        cacheType: CacheType,
        key: String,
        type: Class<T>,
        loader: suspend () -> T
    ): T {
        val data = loader()

        redisClient.publish(RedisTopic.CACHE_EVICT, cacheType.createCacheKey(key))

        return data
    }

    override suspend fun clearAll(cacheType: CacheType) {
        localCacheManager.clearAll(cacheType)
        distributeCacheManager.clearAll(cacheType)
    }
}