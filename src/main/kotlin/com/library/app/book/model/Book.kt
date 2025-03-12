package com.library.app.book.model

import com.library.app.book.dto.BookResponse
import java.time.LocalDateTime

class Book(
    val id: Long? = null,
    val plot: String,
    val title: String,
    val genres: List<String> = listOf(),
    val userId: Long? = null,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
) {
    fun toBookInfo(): BookResponse.BookInfo {
        return BookResponse.BookInfo(
            bookId = id!!,
            title = title,
            plot = plot,
            genres = genres,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}