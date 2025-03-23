package com.library.app.book.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import java.time.LocalDateTime

class BookResponse {
    data class BookAggregateInfo(
        val bookInfo: BookInfo,
        val bookGenreInfo: List<BookGenreInfo>,
    )

    data class BookInfo(
        @JsonProperty("bookId") val bookId: Long,
        @JsonProperty("title") val title: String,
        @JsonProperty("plot") val plot: String,
        @JsonProperty("genres") val genres: List<String>,
        @JsonProperty("createdAt") val createdAt: LocalDateTime? = null,
        @JsonProperty("updatedAt") val updatedAt: LocalDateTime? = null
    ) : Serializable

    data class BookGenreInfo(
        val bookGenreId: Long,
        val genres: List<String>,
        val createdAt: LocalDateTime? = null,
        val updatedAt: LocalDateTime? = null,
    )

    data class BookContentInfo(
        val bookContentId: Long,
        val content: String,
        val createdAt: LocalDateTime? = null,
        val updatedAt: LocalDateTime? = null,
    )
}