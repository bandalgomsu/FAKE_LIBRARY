package com.library.app.common.cache

import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.stereotype.Component

@Component("localCaffeineCacheManager")
class LocalCaffeineCacheManager(
    private val caffeineCacheManager: CaffeineCacheManager,
) : CacheManager {

    override suspend fun <T : Any> getOrLoad(
        cacheType: CacheType,
        key: String,
        type: Class<T>,
        loader: suspend () -> T
    ): T {
        val cacheKey = createCacheKey(cacheType, key)

        val localCache = caffeineCacheManager.getCache(cacheType.cacheName)

        localCache?.get(cacheKey)?.let {
            return it.get() as T
        }

        val value = loader()
        localCache?.put(cacheKey, value)

        return value
    }

    override suspend fun <T : Any> evict(
        cacheType: CacheType,
        key: String,
        type: Class<T>,
        loader: suspend () -> T
    ): T {
        val cacheKey = createCacheKey(cacheType, key)

        val localCache = caffeineCacheManager.getCache(cacheType.cacheName)

        localCache?.evict(cacheKey)

        return loader()
    }

    override suspend fun <T : Any> put(cacheType: CacheType, key: String, type: Class<T>, data: T) {
        val cache = caffeineCacheManager.getCache(cacheType.cacheName)
        val cacheKey = createCacheKey(cacheType, key)

        cache?.put(cacheKey, data)
    }

    private fun createCacheKey(cacheType: CacheType, key: String): String {
        return "${cacheType.cacheName}-$key"
    }
}