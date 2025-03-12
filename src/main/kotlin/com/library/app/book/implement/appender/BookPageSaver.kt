package com.library.app.book.implement.appender

import com.library.app.book.dao.BookPageDao
import com.library.app.book.model.BookPage
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class BookPageSaver(
    private val bookPageDao: BookPageDao
) {
    @Transactional
    suspend fun save(contents: String, bookId: Long): BookPage {
        return bookPageDao.save(
            BookPage(
                contents = contents,
                bookId = bookId
            )
        )
    }
}