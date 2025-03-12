package com.library.app.book.dto

import java.time.LocalDateTime

class BookResponse {
    data class BookAggregateInfo(
        val bookInfo: BookInfo,
        val bookGenreInfo: List<BookGenreInfo>,
    )

    data class BookInfo(
        val bookId: Long,
        val title: String,
        val plot: String,
        val genres: List<String>,
        val createdAt: LocalDateTime? = null,
        val updatedAt: LocalDateTime? = null
    )

    data class BookGenreInfo(
        val bookGenreId: Long,
        val genres: List<String>,
        val createdAt: LocalDateTime? = null,
        val updatedAt: LocalDateTime? = null,
    )

    data class BookPageInfo(
        val bookPageId: Long,
        val contents: String,
        val createdAt: LocalDateTime? = null,
        val updatedAt: LocalDateTime? = null,
    )
}