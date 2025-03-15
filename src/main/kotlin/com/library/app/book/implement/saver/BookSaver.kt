package com.library.app.book.implement.saver

import com.library.app.book.dao.BookDao
import com.library.app.book.model.Book
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class BookSaver(
    private val bookDao: BookDao
) {
    @Transactional
    suspend fun save(plot: String, title: String): Book {
        return bookDao.save(
            Book(
                plot = plot,
                title = title,
            )
        )
    }
}