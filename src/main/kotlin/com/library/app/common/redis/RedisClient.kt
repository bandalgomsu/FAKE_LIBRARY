package com.library.app.common.redis

import kotlin.reflect.KClass

interface RedisClient {
    suspend fun <T> setData(key: String, data: T): Boolean

    suspend fun <T> setData(key: String, data: T, durationSeconds: Long): Boolean

    suspend fun <T : Any> getData(key: String, type: KClass<T>): T?

    suspend fun deleteData(key: String): Boolean
}