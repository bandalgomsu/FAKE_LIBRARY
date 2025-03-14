package com.library.app.common.redis

import org.springframework.stereotype.Component

@Component
class RedisConnectContext(
    var isConnect: Boolean = false
) {

}