package com.library.app.common.cache

interface CacheManager {
    suspend fun <T : Any> getOrLoad(
        cacheType: CacheType,
        key: String,
        type: Class<T>,
        loader: suspend () -> T
    ): T

    suspend fun <T : Any> evict(
        cacheType: CacheType,
        key: String,
        type: Class<T>,
        loader: suspend () -> T
    ): T

    suspend fun <T : Any> put(
        cacheType: CacheType,
        key: String,
        type: Class<T>,
        data: T
    ) {
    }
}