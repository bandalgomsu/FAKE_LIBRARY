package com.library.app.common.cache

import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
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

    @Bean("redisCacheManager")
    fun redisCacheManager(redisConnectionFactory: RedisConnectionFactory): RedisCacheManager {
        val cacheConfigurations = CacheType.entries.associate { cacheType ->
            cacheType.cacheName to RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(cacheType.expireSeconds))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(StringRedisSerializer()))
                .serializeValuesWith(
                    RedisSerializationContext.SerializationPair.fromSerializer(
                        GenericJackson2JsonRedisSerializer()
                    )
                )
        }

        return RedisCacheManager.builder(redisConnectionFactory)
            .withInitialCacheConfigurations(cacheConfigurations)
            .build()
    }
}