package com.library.app.common.cache

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.github.benmanes.caffeine.cache.Caffeine
import com.library.app.common.PageResponse
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
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

//    @Bean("redisCacheManager")
//    fun redisCacheManager(redisConnectionFactory: RedisConnectionFactory): RedisCacheManager {
//        val cacheConfigurations = CacheType.entries.associate { cacheType ->
//            cacheType.cacheName to RedisCacheConfiguration.defaultCacheConfig()
//                .entryTtl(Duration.ofSeconds(cacheType.expireSeconds))
//                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(StringRedisSerializer()))
//                .serializeValuesWith(
//                    RedisSerializationContext.SerializationPair.fromSerializer(
//                        GenericJackson2JsonRedisSerializer()
//                    )
//                )
//        }
//
//        return RedisCacheManager.builder(redisConnectionFactory)
//            .withInitialCacheConfigurations(cacheConfigurations)
//            .build()
//    }

    @Bean("redisCacheManager")
    fun redisCacheManager(redisConnectionFactory: RedisConnectionFactory): RedisCacheManager {
        val objectMapper = ObjectMapper().apply {
            registerModule(JavaTimeModule()) // ✅ LocalDateTime 직렬화 지원
            configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false) // ✅ ISO 8601 포맷 유지
            setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY)

            // ✅ 타입 정보를 JSON 내에 포함하여 역직렬화할 때 정확한 타입을 유지
            activateDefaultTyping(
                BasicPolymorphicTypeValidator.builder()
                    .allowIfBaseType(Any::class.java).build(),
                ObjectMapper.DefaultTyping.NON_FINAL
            )
        }

        // ✅ Jackson2JsonRedisSerializer 설정 (제네릭 타입 유지 가능)
        val jacksonSerializer = Jackson2JsonRedisSerializer(objectMapper, PageResponse::class.java)
        
        val cacheConfigurations = CacheType.entries.associate { cacheType ->
            cacheType.cacheName to RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(cacheType.expireSeconds)) // TTL 적용
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jacksonSerializer)) // ✅ 여기 수정!
        }

        return RedisCacheManager.builder(redisConnectionFactory)
            .withInitialCacheConfigurations(cacheConfigurations)
            .build()
    }

//    @Bean
//    fun objectMapper(): ObjectMapper {
//        val objectMapper = ObjectMapper().apply {
//            registerModule(JavaTimeModule()) // ✅ LocalDateTime 직렬화 지원 추가
//            configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false) // ✅ ISO 8601 포맷 적용
//            setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY)
//        }
//
//        return objectMapper
//    }
}