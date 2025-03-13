package com.library.app.common.cache

import java.time.LocalDateTime
import java.time.ZoneOffset

enum class CacheType(
    val cacheName: String,
    val expireSeconds: Long,
    val redisExpireSeconds: Long,
    val maxSize: Long
) {
    NEW_BOOK(
        "NEW_BOOK",
        LocalDateTime.now().toLocalDate().plusDays(1).atStartOfDay().toEpochSecond(ZoneOffset.UTC)
                - LocalDateTime.now()
            .toEpochSecond(ZoneOffset.UTC),
        LocalDateTime.now().toLocalDate().plusDays(1).atStartOfDay().toEpochSecond(ZoneOffset.UTC)
                - LocalDateTime.now()
            .toEpochSecond(ZoneOffset.UTC),
        10000
    ),
}