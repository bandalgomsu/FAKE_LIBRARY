package com.library.app.book.dao

import com.library.app.book.model.BookPage

interface BookPageDao {
    suspend fun getAllByBookId(bookId: Long): List<BookPage>
    suspend fun save(bookPage: BookPage): BookPage
}