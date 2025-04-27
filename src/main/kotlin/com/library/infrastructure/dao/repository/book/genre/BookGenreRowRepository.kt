package com.library.infrastructure.dao.repository.book.genre

import com.library.app.book.model.BookGenre
import com.library.app.common.PageResponse
import com.library.app.common.exception.BusinessException
import com.library.app.common.exception.CommonErrorCode
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.r2dbc.core.awaitSingle
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class BookGenreRowRepository(
    private val databaseClient: DatabaseClient
) {
    suspend fun findPage(size: Int, page: Int, genres: List<String>): PageResponse<BookGenre> {
        val whereClause = if (genres.isNotEmpty()) {
            "WHERE " + genres.joinToString(" OR ") { "genre LIKE '%$it%'" }
        } else {
            ""
        }

        val offset = page * size
        val sql = "SELECT * FROM book_genre $whereClause LIMIT $size OFFSET $offset"

        val genres = databaseClient.sql(sql)
            .map { row ->
                BookGenre(
                    id = row.get("id", java.lang.Long::class.java)?.toLong()
                        ?: throw BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR),
                    bookId = row.get("book_id", java.lang.Long::class.java)?.toLong() ?: throw BusinessException(
                        CommonErrorCode.INTERNAL_SERVER_ERROR
                    ),
                    genre = row.get("genre", String::class.java)
                        ?: throw BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR),
                    createdAt = row.get("created_at", LocalDateTime::class.java),
                    updatedAt = row.get("updated_at", LocalDateTime::class.java),
                )
            }
            .all()
            .asFlow()
            .toList()

        val countSql = "SELECT COUNT(*) FROM book_genre $whereClause"
        val totalElements = databaseClient.sql(countSql).map { row -> row.get(0, Long::class.java) }.awaitSingle()!!
        val totalPages = (totalElements / size) + if (totalElements % size > 0) 1 else 0

        return PageResponse(
            result = genres,
            totalPages = totalPages,
            totalElements = totalElements,
            currentPage = page,
            pageSize = size
        )
    }
}