package com.library.app.book.dao

import com.library.app.book.model.BookGenre
import com.library.app.common.PageResponse

interface BookGenreDao {
    suspend fun findPage(size: Int = 10, page: Int = 1, genres: List<String>): PageResponse<BookGenre>
    suspend fun getAllByBookId(bookId: Long): List<BookGenre>
    suspend fun getAllInBookIds(bookIds: List<Long>): List<BookGenre>

    suspend fun save(bookGenre: BookGenre): BookGenre
}