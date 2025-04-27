package com.library.infrastructure.dao.repository.book

import com.library.app.book.model.Book
import com.library.app.book.model.BookGenre
import com.library.app.common.exception.BusinessException
import com.library.app.common.exception.CommonErrorCode
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class BookRowRepository(
    private val databaseClient: DatabaseClient
) {

    suspend fun getBookByIdWithGenre(bookId: Long): Pair<Book, List<BookGenre>> {
        val sql = """
            SELECT 
                b.id AS book_id,
                b.plot,
                b.title,
                b.user_id,
                b.created_at AS book_created_at,
                b.updated_at AS book_updated_at,
                
                bg.id AS genre_id,
                bg.book_id,
                bg.genre,
                bg.created_at AS genre_created_at,
                bg.updated_at AS genre_updated_at
            FROM book b
            JOIN book_genre bg ON b.id = bg.book_id
            WHERE b.id = :bookId
        """.trimIndent()

        val response = databaseClient.sql(sql)
            .bind("bookId", bookId)
            .map { row, _ ->
                Pair(
                    Book(
                        id = row.get("id", Long::class.java)?.toLong()
                            ?: throw BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR),
                        plot = row.get("plot", String::class.java)
                            ?: throw BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR),
                        title = row.get("title", String::class.java)
                            ?: throw BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR),
                        userId = row.get("user_id", Long::class.java)?.toLong(),
                        createdAt = row.get("book_created_at", LocalDateTime::class.java),
                        updatedAt = row.get("book_updated_at", LocalDateTime::class.java),
                    ),
                    BookGenre(
                        id = row.get("id", java.lang.Long::class.java)?.toLong()
                            ?: throw BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR),
                        bookId = row.get("book_id", java.lang.Long::class.java)?.toLong() ?: throw BusinessException(
                            CommonErrorCode.INTERNAL_SERVER_ERROR
                        ),
                        genre = row.get("genre", String::class.java)
                            ?: throw BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR),
                        createdAt = row.get("genre_created_at", LocalDateTime::class.java),
                        updatedAt = row.get("genre_updated_at", LocalDateTime::class.java),
                    )
                )
            }
            .all()
            .asFlow()
            .toList()

        return Pair(
            response.first().first,
            response.map { it.second }.toList()
        )
    }
}