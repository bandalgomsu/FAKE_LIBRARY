package com.library.app.book.implement.finder

import com.library.app.book.dao.BookContentDao
import com.library.app.book.model.BookContent
import com.library.app.common.PageResponse
import org.springframework.stereotype.Component

@Component
class BookContentFinder(
    private val bookContentDao: BookContentDao
) {

    suspend fun findPageBookPage(bookId: Long, size: Int, page: Int): PageResponse<BookContent> {
        return bookContentDao.findPageByBookId(bookId, page, size)
    }
}