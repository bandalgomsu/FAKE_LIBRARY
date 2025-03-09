package com.library.app.book.dao

import com.library.app.book.model.Book

interface BookDao {
    suspend fun getById(bookId: Long): Book?

    suspend fun save(book: Book): Book
}