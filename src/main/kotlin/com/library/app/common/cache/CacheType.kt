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
        10000
    ),
}