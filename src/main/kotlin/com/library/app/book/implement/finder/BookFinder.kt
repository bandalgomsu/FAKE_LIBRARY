package com.library.app.book.implement.finder

import com.library.app.book.dao.BookDao
import com.library.app.book.model.Book
import com.library.app.common.PageResponse
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class BookFinder(
    private val bookDao: BookDao
) {
    suspend fun findPage(size: Int = 1, page: Int = 1): PageResponse<Book> {
        return bookDao.findPage(size, page)
    }

    suspend fun findPageNewBook(size: Int = 1, page: Int = 1): PageResponse<Book> {
        return bookDao.findPageByCreatedAtBetween(
            size,
            page,
            LocalDate.now().atStartOfDay(),
            LocalDate.now().atStartOfDay().plusDays(1)
        )
    }
}