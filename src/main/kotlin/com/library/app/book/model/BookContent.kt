package com.library.app.book.model

import java.time.LocalDateTime

class BookContent(
    val id: Long? = null,
    val content: String,
    val bookId: Long,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
) {
}