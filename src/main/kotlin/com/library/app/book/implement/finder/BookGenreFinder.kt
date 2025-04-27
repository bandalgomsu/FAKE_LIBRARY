package com.library.app.book.implement.finder

import com.library.app.book.dao.BookGenreDao
import com.library.app.book.model.BookGenre
import com.library.app.common.PageResponse
import org.springframework.stereotype.Component

@Component
class BookGenreFinder(
    private val bookGenreDao: BookGenreDao
) {

    suspend fun findPage(size: Int = 10, page: Int = 1, genres: List<String>): PageResponse<BookGenre> {
        return bookGenreDao.findPage(size, page, genres)
    }
}