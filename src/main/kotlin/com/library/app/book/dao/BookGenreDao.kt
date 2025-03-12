package com.library.app.book.dao

import com.library.app.book.model.BookGenre

interface BookGenreDao {
    suspend fun getAllByBookId(bookId: Long): List<BookGenre>
    suspend fun save(bookGenre: BookGenre): BookGenre
}