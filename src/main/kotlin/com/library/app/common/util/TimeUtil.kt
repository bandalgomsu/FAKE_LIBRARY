package com.library.app.common.util

import java.time.LocalDateTime
import java.time.ZoneOffset

class TimeUtil {
    companion object {
        fun getRemainingSecondsToday(): Long {
            return LocalDateTime.now().toLocalDate().plusDays(1).atStartOfDay()
                .toEpochSecond(ZoneOffset.UTC) - LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
        }
    }
}