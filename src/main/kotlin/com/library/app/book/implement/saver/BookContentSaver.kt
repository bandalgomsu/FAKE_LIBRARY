package com.library.app.book.implement.saver

import com.library.app.book.dao.BookContentDao
import com.library.app.book.model.BookContent
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class BookContentSaver(
    private val bookContentDao: BookContentDao
) {
    @Transactional
    suspend fun save(contents: String, bookId: Long): BookContent {
        return bookContentDao.save(
            BookContent(
                content = contents,
                bookId = bookId
            )
        )
    }
}