package com.library.app.common.cache

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.cache.Cache
import org.springframework.cache.caffeine.CaffeineCacheManager
import kotlin.test.assertEquals

class LocalCaffeineCacheManagerTest {
    private lateinit var localCaffeineCacheManager: LocalCaffeineCacheManager
    private lateinit var caffeineCacheManager: CaffeineCacheManager
    private lateinit var cache: Cache

    @BeforeEach
    fun setup() {
        caffeineCacheManager = mockk(relaxed = true)
        cache = mockk(relaxed = true)

        every { caffeineCacheManager.getCache(any()) } returns cache

        localCaffeineCacheManager = LocalCaffeineCacheManager(caffeineCacheManager)
    }

    @Test
    fun `GET_OR_LOAD_HIT`() = runTest {
        val cacheType = CacheType.NEW_BOOK
        val key = "test"
        val expectedValue = "value"

        val cacheValue = mockk<Cache.ValueWrapper>()
        every { cache.get(cacheType.createCacheKey(key)) } returns cacheValue
        every { cacheValue.get() } returns expectedValue

        val result = localCaffeineCacheManager.getOrLoad(cacheType, key, String::class.java) { "newValue" }

        assertEquals(expectedValue, result)
        verify(exactly = 1) { cache.get(cacheType.createCacheKey(key)) }
        verify(exactly = 0) { cache.put(any(), any()) }
    }

    @Test
    fun `GET_OR_LOAD_MISS`() = runTest {
        val cacheType = CacheType.NEW_BOOK
        val key = "test"
        val expectedValue = "newValue"

        every { cache.get(cacheType.createCacheKey(key)) } returns null

        val result = localCaffeineCacheManager.getOrLoad(cacheType, key, String::class.java) { "newValue" }

        assertEquals(expectedValue, result)

        verify(exactly = 1) { cache.put(any(), any()) }
    }

    @Test
    fun `EVICT`() = runTest {
        val cacheType = CacheType.NEW_BOOK
        val key = "test"
        val expectedValue = "newValue"

        every { caffeineCacheManager.getCache(any()) } returns cache

        val result = localCaffeineCacheManager.evict(cacheType, key, String::class.java) { "newValue" }

        verify(exactly = 1) { cache.evict(cacheType.createCacheKey(key)) }
    }

    @Test
    fun `PUT`() = runTest {
        val cacheType = CacheType.NEW_BOOK
        val key = "test"
        val expectedValue = "newValue"

        every { caffeineCacheManager.getCache(any()) } returns cache

        val result = localCaffeineCacheManager.put(cacheType, key, String::class.java, expectedValue)

        verify(exactly = 1) { cache.put(cacheType.createCacheKey(key), expectedValue) }
    }
}