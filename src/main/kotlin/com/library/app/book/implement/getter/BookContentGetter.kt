package com.library.app.book.implement.getter

import com.library.app.book.dao.BookContentDao
import com.library.app.book.model.BookContent
import org.springframework.stereotype.Component

@Component
class BookContentGetter(
    private val bookContentDao: BookContentDao
) {
    suspend fun getAllByBookId(bookId: Long): List<BookContent> {
        return bookContentDao.getAllByBookId(bookId)
    }
}