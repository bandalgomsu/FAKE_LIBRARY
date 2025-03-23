package com.library.app.book.implement.finder

import com.library.app.book.BookTestFactory
import com.library.app.book.dao.BookDao
import com.library.app.common.PageResponse
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class BookFinderTest {

    private lateinit var bookDao: BookDao

    private lateinit var bookFinder: BookFinder

    @BeforeEach
    fun setup() = runTest {
        bookDao = mockk(relaxed = true)

        bookFinder = BookFinder(bookDao)
    }

    @Test
    fun `FIND_PAGE`() = runTest {
        val size = 1
        val page = 1

        val book = BookTestFactory.createTestBook()

        val pageResponse = PageResponse(
            result = listOf(book),
            totalPages = 1,
            totalElements = 1,
            currentPage = page,
            pageSize = size
        )

        coEvery {
            bookDao.findPage(size, page)
        } returns pageResponse

        val result = bookFinder.findPage(size, page)

        assertEquals(1, result.result.size)
        assertEquals(book, result.result[0])
        assertEquals(1, result.totalPages)
        assertEquals(1, result.totalElements)
        assertEquals(page, result.currentPage)
        assertEquals(size, result.pageSize)
    }

    @Test
    fun `FIND_PAGE_NEW_BOOK`() = runTest {
        val size = 1
        val page = 1

        val book = BookTestFactory.createTestBook()

        val pageResponse = PageResponse(
            result = listOf(book),
            totalPages = 1,
            totalElements = 1,
            currentPage = page,
            pageSize = size
        )

        coEvery {
            bookDao.findPageByCreatedAtBetween(size, page, any(), any())
        } returns pageResponse

        val result = bookFinder.findPageNewBook(size, page)

        assertEquals(1, result.result.size)
        assertEquals(book, result.result[0])
        
        assertEquals(1, result.totalPages)
        assertEquals(1, result.totalElements)
        assertEquals(page, result.currentPage)
        assertEquals(size, result.pageSize)
    }
}