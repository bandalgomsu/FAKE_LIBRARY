package com.library.app.book.implement.finder

import com.library.app.book.BookTestFactory
import com.library.app.book.dao.BookContentDao
import com.library.app.common.PageResponse
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class BookContentFinderTest {
    private lateinit var bookContentDao: BookContentDao

    private lateinit var bookContentFinder: BookContentFinder

    @BeforeEach
    fun setup() = runTest {
        bookContentDao = mockk(relaxed = true)

        bookContentFinder = BookContentFinder(bookContentDao)
    }

    @Test
    fun `FIND_PAGE_BY_BOOK_ID`() = runTest {
        val size = 1
        val page = 1

        val bookContent = BookTestFactory.createTestBookContent()

        val pageResponse = PageResponse(
            result = listOf(bookContent),
            totalPages = 1,
            totalElements = 1,
            currentPage = page,
            pageSize = size
        )

        coEvery {
            bookContentFinder.findPageByBookId(BookTestFactory.bookId, size, page)
        } returns pageResponse

        val result = bookContentFinder.findPageByBookId(BookTestFactory.bookId, size, page)

        assertEquals(1, result.result.size)
        assertEquals(bookContent, result.result[0])
        assertEquals(1, result.totalPages)
        assertEquals(1, result.totalElements)
        assertEquals(page, result.currentPage)
        assertEquals(size, result.pageSize)
    }
}