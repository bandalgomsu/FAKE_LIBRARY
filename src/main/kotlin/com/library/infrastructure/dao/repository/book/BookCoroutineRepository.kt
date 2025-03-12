package com.library.infrastructure.dao.repository.book

import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.time.LocalDateTime

interface BookCoroutineRepository : CoroutineCrudRepository<BookEntity, Long> {
    suspend fun findAllById(bookId: Long): Flow<BookEntity>

    @Query(
        """
        SELECT * FROM book
        ORDER BY created_at DESC 
        LIMIT :size OFFSET :offset
    """
    )
    suspend fun findPage(size: Int, offset: Int): Flow<BookEntity>

    @Query(
        """
    SELECT * FROM book 
    WHERE created_at BETWEEN :startDate AND :endDate
    ORDER BY created_at DESC
    LIMIT :size OFFSET :offset
    """
    )
    suspend fun findPageByCreatedAtRange(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        size: Int,
        offset: Int
    ): Flow<BookEntity>

    @Query(
        """
    SELECT COUNT(*) FROM book 
    WHERE created_at BETWEEN :startDate AND :endDate
    """
    )
    suspend fun countByCreatedAtBetween(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        size: Int,
        offset: Int
    ): Long
}