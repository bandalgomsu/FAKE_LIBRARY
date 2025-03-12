package com.library.app.book.implement.finder

import com.library.app.book.dao.BookPageDao
import com.library.app.book.model.BookPage
import com.library.app.common.PageResponse
import org.springframework.stereotype.Component

@Component
class BookPageFinder(
    private val bookPageDao: BookPageDao
) {

    suspend fun findPageBookPage(bookId: Long, page: Int, size: Int): PageResponse<BookPage> {
        return bookPageDao.findPageByBookId(bookId, page, size)
    }
}