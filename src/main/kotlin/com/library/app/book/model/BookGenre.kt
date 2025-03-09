package com.library.app.book.model

import java.time.LocalDateTime

class BookGenre(
    val id: Long? = null,
    val genre: String,
    val bookId: Long,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
) {
}