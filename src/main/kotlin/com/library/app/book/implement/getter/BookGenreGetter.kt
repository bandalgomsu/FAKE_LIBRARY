package com.library.app.book.implement.getter

import com.library.app.book.dao.BookGenreDao
import com.library.app.book.model.BookGenre
import org.springframework.stereotype.Component

@Component
class BookGenreGetter(
    private val bookGenreDao: BookGenreDao
) {
    suspend fun getAllByBookId(bookId: Long): List<BookGenre> {
        return bookGenreDao.getAllByBookId(bookId)
    }
    
}