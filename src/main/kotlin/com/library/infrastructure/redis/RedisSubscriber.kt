package com.library.infrastructure.redis

import com.fasterxml.jackson.databind.ObjectMapper
import com.library.app.common.redis.RedisTopic
import jakarta.annotation.PostConstruct
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactive.collect
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.data.redis.listener.ReactiveRedisMessageListenerContainer
import org.springframework.stereotype.Component

@Component
class RedisSubscriber(
    private val connectionFactory: ReactiveRedisConnectionFactory,

    private val caffeineCacheManager: CaffeineCacheManager,
    private val objectMapper: ObjectMapper
) {

    @PostConstruct
    fun init() {
        CoroutineScope(Dispatchers.IO).launch {
            subscribe()
        }
    }

    suspend fun subscribe() {
        val listenerContainer = ReactiveRedisMessageListenerContainer(connectionFactory)

        val channels = RedisTopic.entries.map {
            ChannelTopic(it.name)
        }

        listenerContainer.receive(*channels.toTypedArray())
            .collect {
                if (it.channel == RedisTopic.CACHE_EVICT.name) {
                    val (cacheName, cacheKey) = it.message.split("-")

                    caffeineCacheManager.getCache(cacheName)?.evict(cacheKey)
                }
            }
    }
}