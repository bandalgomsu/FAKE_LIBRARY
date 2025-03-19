package com.library.app.book

import com.library.app.book.model.Book
import com.library.app.book.model.BookContent
import com.library.app.book.model.BookGenre
import java.time.LocalDateTime

class BookTestFactory {
    companion object {
        const val bookId: Long = 1
        const val plot: String = "P1"
        const val title: String = "T1"

        const val userId: Long = 1

        const val genreId: Long = 2
        const val contentId: Long = 2

        const val genre: String = "G1"
        const val content: String = "C1"

        val createdAt: LocalDateTime = LocalDateTime.of(2025, 3, 19, 1, 1)
        val updatedAt: LocalDateTime = LocalDateTime.of(2025, 3, 19, 1, 1)

        fun createTestBook(): Book {
            return Book(
                id = bookId,
                plot = plot,
                title = title,
                userId = userId,
                createdAt = createdAt,
                updatedAt = updatedAt
            )
        }

        fun createTestBookGenre(): BookGenre {
            return BookGenre(
                id = genreId,
                genre = genre,
                bookId = bookId,
                createdAt = createdAt,
                updatedAt = updatedAt,
            )
        }

        fun createTestBookContent(): BookContent {
            return BookContent(
                id = contentId,
                content = content,
                bookId = bookId,
                createdAt = createdAt,
                updatedAt = updatedAt,
            )
        }
    }
}