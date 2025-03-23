package com.library.app.common.cache

import com.library.app.common.redis.RedisClient
import com.library.app.common.redis.RedisConnectContext
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class TwoLevelCacheManagerTest {
    private lateinit var distributeRedisCacheManager: DistributeRedisCacheManager
    private lateinit var localCaffeineCacheManager: LocalCaffeineCacheManager
    private lateinit var redisClient: RedisClient
    private var redisConnectContext: RedisConnectContext = RedisConnectContext(true)

    private lateinit var twoLevelCacheManager: TwoLevelCacheManager


    @BeforeEach
    fun setup() {
        distributeRedisCacheManager = mockk(relaxed = true)
        localCaffeineCacheManager = mockk(relaxed = true)
        redisClient = mockk(relaxed = true)


        twoLevelCacheManager = TwoLevelCacheManager(
            localCaffeineCacheManager,
            distributeRedisCacheManager,
            redisClient,
            redisConnectContext
        )
    }

    @Test
    fun `GET_OR_LOAD_LOCAL_CACHE_HIT`() = runTest {
        val cacheType = CacheType.NEW_BOOK
        val key = "test"
        val expectedValue = "value"

        coEvery {
            localCaffeineCacheManager.getOrLoad(
                cacheType,
                key,
                String::class.java,
                any()
            )
        } returns expectedValue

        val result = twoLevelCacheManager.getOrLoad(cacheType, key, String::class.java) { "newValue" }

        assertEquals(expectedValue, result)
    }

    @Test
    fun `GET_OR_LOAD_DISTRIBUTE_CACHE_HIT`() = runTest {
        val cacheType = CacheType.NEW_BOOK
        val key = "test"
        val expectedValue = "value"

        coEvery {
            distributeRedisCacheManager.getOrLoad(cacheType, key, String::class.java, any())
        } returns expectedValue

        coEvery {
            localCaffeineCacheManager.getOrLoad(
                cacheType,
                key,
                String::class.java,
                any()
            )
        } returns distributeRedisCacheManager.getOrLoad(cacheType, key, String::class.java) { "newValue" }


        val result = twoLevelCacheManager.getOrLoad(cacheType, key, String::class.java) { "newValue" }

        assertEquals(expectedValue, result)
    }

    @Test
    fun `GET_OR_LOAD_MISS`() = runTest {
        val cacheType = CacheType.NEW_BOOK
        val key = "test"
        val expectedValue = "newValue"

        val function = { "newValue" }

        coEvery {
            distributeRedisCacheManager.getOrLoad(cacheType, key, String::class.java, function)
        } returns function()

        coEvery {
            localCaffeineCacheManager.getOrLoad(
                cacheType,
                key,
                String::class.java,
                any()
            )
        } returns distributeRedisCacheManager.getOrLoad(
            cacheType, key, String::class.java, function
        )

        val result = twoLevelCacheManager.getOrLoad(cacheType, key, String::class.java, function)

        assertEquals(expectedValue, result)
    }

    @Test
    fun `GET_OR_LOAD_DISCONNECT_REDIS`() = runTest {
        val cacheType = CacheType.NEW_BOOK
        val key = "test"
        val expectedValue = "newValue"

        val function = { "newValue" }

        redisConnectContext.isConnect = false

        val result = twoLevelCacheManager.getOrLoad(cacheType, key, String::class.java, function)

        assertEquals(expectedValue, result)
        coVerify(exactly = 0) { localCaffeineCacheManager.getOrLoad(cacheType, key, String::class.java, function) }
        coVerify(exactly = 0) { distributeRedisCacheManager.getOrLoad(cacheType, key, String::class.java, function) }
    }
}