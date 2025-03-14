package com.library.app.common.cache

import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import java.time.Duration

@Configuration
@EnableCaching
class CacheConfig(
) {

    @Primary
    @Bean("caffeineCacheManager")
    fun caffeineCacheManager(): CaffeineCacheManager {
        val caffeineCacheManager = CaffeineCacheManager()

        caffeineCacheManager.isAllowNullValues = true

        CacheType.entries.forEach {
            val caffeineCache = Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofSeconds(it.expireSeconds))
                .maximumSize(it.maxSize)
                .build<Any, Any>()

            caffeineCacheManager
                .registerCustomCache(it.name, caffeineCache)
        }

        return caffeineCacheManager
    }
}