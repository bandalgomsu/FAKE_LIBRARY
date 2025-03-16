package com.library.infrastructure.dao.repository.book.content

import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface BookContentCoroutineRepository : CoroutineCrudRepository<BookContentEntity, Long> {
    suspend fun findAllByBookId(bookId: Long): Flow<BookContentEntity>


    @Query("SELECT COUNT(*) FROM book_content WHERE book_id = :bookId")
    suspend fun countByBookId(bookId: Long): Long

    @Query(
        """
        SELECT * FROM book_content
        WHERE book_id = :bookId 
        ORDER BY created_at DESC 
        LIMIT :size OFFSET :offset
    """
    )
    suspend fun findPageByBookId(bookId: Long, size: Int, offset: Int): Flow<BookContentEntity>
}