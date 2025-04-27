package com.library.infrastructure.dao.repository.book.genre

import com.library.infrastructure.dao.repository.book.BookEntity
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface BookGenreCoroutineRepository : CoroutineCrudRepository<BookGenreEntity, Long> {
    @Query(
        """
        SELECT * FROM book_genre
        WHERE genre LIKE 
        ORDER BY created_at DESC 
        LIMIT :size OFFSET :offset
    """
    )
    suspend fun findPage(size: Int, offset: Int): Flow<BookEntity>
    suspend fun findAllByBookId(bookId: Long): Flow<BookGenreEntity>

    suspend fun findAllByBookIdIn(bookIds: List<Long>): Flow<BookGenreEntity>
}