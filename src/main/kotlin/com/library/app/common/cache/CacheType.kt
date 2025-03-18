package com.library.app.common.cache

import com.library.app.common.util.TimeUtil

enum class CacheType(
    val cacheName: String,
    val expireSeconds: Long,
    val redisExpireSeconds: Long,
    val maxSize: Long
) {
    NEW_BOOK(
        "NEW_BOOK",
        TimeUtil.getRemainingSecondsToday(),
        TimeUtil.getRemainingSecondsToday(),
        1000
    ),
    BOOK_CONTENT(
        "BOOK_CONTENT",
        60 * 5,
        60 * 60,
        2000
    );

    fun createCacheKey(key: String): String {
        return "$cacheName-$key"
    }
}