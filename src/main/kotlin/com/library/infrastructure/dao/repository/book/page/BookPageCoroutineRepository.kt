package com.library.infrastructure.dao.repository.book.page

import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface BookPageCoroutineRepository : CoroutineCrudRepository<BookPageEntity, Long> {
    suspend fun findAllByBookId(bookId: Long): Flow<BookPageEntity>
}