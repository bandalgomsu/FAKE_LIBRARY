package com.library.app.book.dao

import com.library.app.book.model.BookContent
import com.library.app.common.PageResponse

interface BookContentDao {
    suspend fun getAllByBookId(bookId: Long): List<BookContent>

    suspend fun findPageByBookId(bookId: Long, size: Int, page: Int): PageResponse<BookContent>
    suspend fun save(bookContent: BookContent): BookContent
}