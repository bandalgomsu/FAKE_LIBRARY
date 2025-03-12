package com.library.app.book.implement.getter

import com.library.app.book.dao.BookPageDao
import com.library.app.book.model.BookPage
import org.springframework.stereotype.Component

@Component
class BookPageGetter(
    private val bookPageDao: BookPageDao
) {
    suspend fun getAllByBookId(bookId: Long): List<BookPage> {
        return bookPageDao.getAllByBookId(bookId)
    }
}