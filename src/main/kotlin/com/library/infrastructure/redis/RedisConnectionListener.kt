package com.library.infrastructure.redis

import com.library.app.common.redis.RedisClient
import com.library.app.common.redis.RedisConnectContext
import com.library.app.common.redis.RedisTopic
import io.lettuce.core.event.connection.ConnectedEvent
import io.lettuce.core.event.connection.DisconnectedEvent
import io.lettuce.core.resource.ClientResources
import jakarta.annotation.PostConstruct
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class RedisConnectionListener(
    private val redisConnectContext: RedisConnectContext,
    private val redisClient: RedisClient,
    clientResources: ClientResources,
) {
    private val eventBus = clientResources.eventBus()

    private val logger = LoggerFactory.getLogger(RedisConnectionListener::class.java)

    @PostConstruct
    fun init() {
        CoroutineScope(Dispatchers.IO).launch {
            eventBus.get().subscribe { event ->
                when (event) {
                    is DisconnectedEvent -> {
                        logger.error("REDIS_DISCONNECTED: ${event.remoteAddress()}")
                        redisConnectContext.isConnect = false
                    }

                    is ConnectedEvent -> {
                        logger.info("REDIS_CONNECTED: ${event.remoteAddress()}")
                        redisConnectContext.isConnect = true

                        CoroutineScope(Dispatchers.IO).launch {
                            redisClient.publish(RedisTopic.LOCAL_CACHE_ALL_CLEAR, "CLEAN")
                        }
                    }
                }
            }
        }
    }
}
