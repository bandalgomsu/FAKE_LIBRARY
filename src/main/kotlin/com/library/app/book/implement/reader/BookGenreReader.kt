package com.library.app.book.implement.reader

import com.library.app.book.dao.BookGenreDao
import com.library.app.book.model.BookGenre
import org.springframework.stereotype.Component

@Component
class BookGenreReader(
    private val bookGenreDao: BookGenreDao
) {
    suspend fun getByBookId(bookId: Long): List<BookGenre> {
        return bookGenreDao.getAllByBookId(bookId)
    }
}