package com.library.app.book.implement.appender

import com.library.app.book.dao.BookGenreDao
import com.library.app.book.model.BookGenre
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class BookGenreAppender(
    private val bookGenreDao: BookGenreDao
) {
    @Transactional
    suspend fun save(genre: String, bookId: Long): BookGenre {
        return bookGenreDao.save(
            BookGenre(
                genre = genre,
                bookId = bookId
            )
        )
    }
}