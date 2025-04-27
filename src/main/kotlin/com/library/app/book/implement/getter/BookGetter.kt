package com.library.app.book.implement.getter

import com.library.app.book.dao.BookDao
import com.library.app.book.exception.BookErrorCode
import com.library.app.book.model.Book
import com.library.app.common.exception.BusinessException
import org.springframework.stereotype.Component

@Component
class BookGetter(
    private val bookDao: BookDao
) {

    suspend fun getBookById(bookId: Long): Book {
        return bookDao.getById(bookId) ?: throw BusinessException(BookErrorCode.BOOK_NOT_FOUND)
    }
}