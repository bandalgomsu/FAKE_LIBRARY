package com.library.app.book.model

import java.time.LocalDateTime

class Book(
    val id: Long? = null,
    val plot: String,
    val title: String,
    val userId: Long? = null,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
) {
}