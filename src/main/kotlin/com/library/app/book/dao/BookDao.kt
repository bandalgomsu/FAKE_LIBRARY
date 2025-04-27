package com.library.app.book.dao

import com.library.app.book.model.Book
import com.library.app.common.PageResponse
import java.time.LocalDateTime

interface BookDao {
    suspend fun getById(bookId: Long): Book?
    
    suspend fun findPage(size: Int, page: Int): PageResponse<Book>
    suspend fun findPageByCreatedAtBetween(
        size: Int,
        page: Int,
        startDate: LocalDateTime,
        endData: LocalDateTime
    ): PageResponse<Book>

    suspend fun save(book: Book): Book
}