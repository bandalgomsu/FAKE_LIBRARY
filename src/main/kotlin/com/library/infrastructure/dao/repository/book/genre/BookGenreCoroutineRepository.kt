package com.library.infrastructure.dao.repository.book.genre

import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface BookGenreCoroutineRepository : CoroutineCrudRepository<BookGenreEntity, Long> {
    suspend fun findAllByBookId(bookId: Long): Flow<BookGenreEntity>
}