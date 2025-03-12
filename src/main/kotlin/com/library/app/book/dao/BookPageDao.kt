package com.library.app.book.dao

import com.library.app.book.model.BookPage
import com.library.app.common.PageResponse

interface BookPageDao {
    suspend fun getAllByBookId(bookId: Long): List<BookPage>

    suspend fun findPageByBookId(bookId: Long, size: Int, page: Int): PageResponse<BookPage>
    suspend fun save(bookPage: BookPage): BookPage
}