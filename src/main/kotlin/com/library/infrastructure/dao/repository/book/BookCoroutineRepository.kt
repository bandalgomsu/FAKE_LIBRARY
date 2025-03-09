package com.library.infrastructure.dao.repository.book

import com.library.app.book.model.Book
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface BookCoroutineRepository : CoroutineCrudRepository<BookEntity, Long> {
    suspend fun findAllById(bookId: Long): Flow<Book>
}