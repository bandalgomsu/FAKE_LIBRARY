package com.library.infrastructure.dao.repository.book.page

import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface BookPageCoroutineRepository : CoroutineCrudRepository<BookPageEntity, Long> {
    suspend fun findAllByBookId(bookId: Long): Flow<BookPageEntity>


    @Query("SELECT COUNT(*) FROM book_page WHERE book_id = :bookId")
    suspend fun countByBookId(bookId: Long): Long

    @Query(
        """
        SELECT * FROM book_page 
        WHERE book_id = :bookId 
        ORDER BY created_at DESC 
        LIMIT :size OFFSET :offset
    """
    )
    suspend fun findPageByBookId(bookId: Long, size: Int, offSet: Int): Flow<BookPageEntity>
}