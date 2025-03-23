package com.library.app.common.cache

import com.library.app.common.redis.RedisClient
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class DistributeRedisCacheManagerTest {
    private lateinit var redisClient: RedisClient
    private lateinit var distributeRedisCacheManager: DistributeRedisCacheManager

    @BeforeEach
    fun setup() {
        redisClient = mockk(relaxed = true)

        distributeRedisCacheManager = DistributeRedisCacheManager(redisClient)
    }

    @Test
    fun `GET_OR_LOAD_HIT`() = runTest {
        val cacheType = CacheType.NEW_BOOK
        val key = "test"
        val expectedValue = "value"

        coEvery { redisClient.getData(cacheType.createCacheKey(key), String::class.java) } returns expectedValue

        val result = distributeRedisCacheManager.getOrLoad(cacheType, key, String::class.java) { "newValue" }

        assertEquals(expectedValue, result)
        coVerify(exactly = 0) {
            redisClient.setData(
                cacheType.createCacheKey(key),
                "newValue",
                cacheType.redisExpireSeconds
            )
        }
    }

    @Test
    fun `GET_OR_LOAD_MISS`() = runTest {
        val cacheType = CacheType.NEW_BOOK
        val key = "test"
        val expectedValue = "newValue"

        coEvery {
            redisClient.getData(cacheType.createCacheKey(key), String::class.java)
        } returns null

        coEvery {
            redisClient.setData(
                cacheType.createCacheKey(key),
                expectedValue,
                cacheType.redisExpireSeconds
            )
        } returns true

        val result = distributeRedisCacheManager.getOrLoad(cacheType, key, String::class.java) { expectedValue }

        assertEquals(expectedValue, result)

        coVerify(exactly = 1) {
            redisClient.setData(
                cacheType.createCacheKey(key),
                expectedValue,
                cacheType.redisExpireSeconds
            )
        }
    }

    @Test
    fun `EVICT`() = runTest {
        val cacheType = CacheType.NEW_BOOK
        val key = "test"
        val expectedValue = "newValue"

        distributeRedisCacheManager.evict(cacheType, key, String::class.java) { expectedValue }

        coVerify(exactly = 1) {
            redisClient.deleteData(
                cacheType.createCacheKey(key),
            )
        }
    }


}