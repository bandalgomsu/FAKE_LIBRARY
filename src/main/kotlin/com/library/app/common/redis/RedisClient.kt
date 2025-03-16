package com.library.app.common.redis

import kotlin.reflect.KClass

interface RedisClient {
    suspend fun <T> setData(key: String, data: T): Boolean

    suspend fun <T> setData(key: String, data: T, durationSeconds: Long): Boolean

    suspend fun <T : Any> getData(key: String, type: KClass<T>): T?
    suspend fun <T : Any> getData(key: String, type: Class<T>): T?
    suspend fun getData(key: String): Any?
    suspend fun deleteData(key: String): Boolean
    suspend fun deleteAllByPattern(pattern: String): Boolean
    suspend fun publish(topic: RedisTopic, message: String)
}