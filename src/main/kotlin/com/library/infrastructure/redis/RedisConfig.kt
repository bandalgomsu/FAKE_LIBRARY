package com.library.infrastructure.redis

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.lettuce.core.resource.ClientResources
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration
import org.springframework.boot.autoconfigure.data.redis.RedisReactiveAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
@EnableAutoConfiguration(exclude = [RedisAutoConfiguration::class, RedisReactiveAutoConfiguration::class])
class RedisConfig {
    @Value("\${spring.data.redis.host}")
    private val host: String? = null

    @Value("\${spring.data.redis.port}")
    private val port = 0

    @Bean
    fun clientResources(): ClientResources {
        return ClientResources.create()
    }

    @Bean
    fun redisConnectionFactory(): ReactiveRedisConnectionFactory {
        return LettuceConnectionFactory(
            LettuceConnectionFactory.createRedisConfiguration("redis://$host:$port"),
            LettuceClientConfiguration.builder().clientResources(clientResources()).build()
        )
    }

    @Bean
    fun reactiveRedisAnyTemplate(): ReactiveRedisTemplate<String, Any> {
        val objectMapper = jacksonObjectMapper().apply {
            registerModule(com.fasterxml.jackson.module.kotlin.KotlinModule.Builder().build())

            // ✅ JSON 직렬화 시 @class 필드를 포함하여 타입 정보를 유지하도록 설정
            activateDefaultTyping(
                BasicPolymorphicTypeValidator.builder()
                    .allowIfBaseType(Any::class.java)
                    .build(),
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY
            )
        }

        val serializer = Jackson2JsonRedisSerializer(objectMapper, Any::class.java)

        val builder = RedisSerializationContext
            .newSerializationContext<String, Any>(StringRedisSerializer())

        val context = builder
            .value(serializer) // ✅ 제네릭 타입 유지 가능
            .hashValue(serializer)
            .hashKey(serializer)
            .build()

        return ReactiveRedisTemplate(redisConnectionFactory(), context)
    }

    @Bean
    fun reactiveRedisStringTemplate(): ReactiveRedisTemplate<String, String> {
        val factory = redisConnectionFactory()
        val serializer = StringRedisSerializer()

        val builder = RedisSerializationContext
            .newSerializationContext<String, String>(StringRedisSerializer())

        val context = builder
            .value(serializer)
            .hashValue(serializer)
            .hashKey(StringRedisSerializer())  // hashKey에 대해 문자열 serializer 사용
            .build()

        return ReactiveRedisTemplate(factory, context)
    }
    
}