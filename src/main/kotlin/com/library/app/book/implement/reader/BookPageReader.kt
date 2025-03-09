package com.library.app.book.implement.reader

import com.library.app.book.dao.BookPageDao
import com.library.app.book.model.BookPage
import org.springframework.stereotype.Component

@Component
class BookPageReader(
    private val bookPageDao: BookPageDao
) {
    suspend fun getByBookId(bookId: Long): List<BookPage> {
        return bookPageDao.getAllByBookId(bookId)
    }
}