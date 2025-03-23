package com.library.app.book.service

import com.library.app.book.BookTestFactory
import com.library.app.book.dto.BookPageResponse
import com.library.app.book.implement.finder.BookContentFinder
import com.library.app.book.implement.finder.BookFinder
import com.library.app.book.implement.getter.BookGenreGetter
import com.library.app.common.PageResponse
import com.library.app.common.cache.CacheType
import com.library.app.common.cache.TwoLevelCacheManager
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class BookQueryServiceTest {
    private lateinit var bookFinder: BookFinder
    private lateinit var bookContentFinder: BookContentFinder
    private lateinit var bookGenreGetter: BookGenreGetter
    private lateinit var twoLevelCacheManager: TwoLevelCacheManager

    private lateinit var bookQueryService: BookQueryService

    @BeforeEach
    fun setup() = runTest {
        bookFinder = mockk(relaxed = true)
        bookContentFinder = mockk(relaxed = true)
        bookGenreGetter = mockk(relaxed = true)
        twoLevelCacheManager = mockk(relaxed = true)

        bookQueryService = BookQueryService(bookFinder, bookContentFinder, bookGenreGetter, twoLevelCacheManager)
    }

    @Test
    fun `FIND_PAGE_BOOK`() = runTest {
        val size = 10
        val page = 1

        val book = BookTestFactory.createTestBook()

        val pageResponse = PageResponse(
            result = listOf(book),
            totalPages = 1,
            totalElements = 1,
            currentPage = page,
            pageSize = size
        )

        val genres = listOf(BookTestFactory.createTestBookGenre())

        coEvery { bookFinder.findPage(size, page) } returns pageResponse
        coEvery { bookGenreGetter.getAllByBookId(book.id!!) } returns genres

        val result = bookQueryService.findPageBook(size, page)

        assertEquals(1, result.bookInfos.size)
        assertEquals(book.id, result.bookInfos[0].bookId)
        assertEquals(book.plot, result.bookInfos[0].plot)
        assertEquals(book.title, result.bookInfos[0].title)
        assertEquals(genres[0].genre, result.bookInfos[0].genres[0])
        assertEquals(1, result.totalPages)
        assertEquals(1, result.totalElements)
        assertEquals(page, result.currentPage)
        assertEquals(size, result.pageSize)
    }

    @Test
    fun `FIND_PAGE_NEW_BOOK`() = runTest {
        val size = 10
        val page = 1

        val book = BookTestFactory.createTestBook()

        val pageResponse = PageResponse(
            result = listOf(book),
            totalPages = 1,
            totalElements = 1,
            currentPage = page,
            pageSize = size
        )

        val genres = listOf(BookTestFactory.createTestBookGenre())

        coEvery {
            twoLevelCacheManager.getOrLoad(
                CacheType.NEW_BOOK,
                "$page$size",
                BookPageResponse.BookInfoPagination::class.java,
                any()
            )
        } coAnswers {
            val loader = args[3] as (suspend () -> BookPageResponse.BookInfoPagination)
            loader()
        }

        coEvery { bookFinder.findPageNewBook(size, page) } returns pageResponse
        coEvery { bookGenreGetter.getAllByBookId(book.id!!) } returns genres

        val result = bookQueryService.findPageNewBook(size, page)

        assertEquals(1, result.bookInfos.size)
        assertEquals(book.id, result.bookInfos[0].bookId)
        assertEquals(book.plot, result.bookInfos[0].plot)
        assertEquals(book.title, result.bookInfos[0].title)
        assertEquals(genres[0].genre, result.bookInfos[0].genres[0])
        assertEquals(1, result.totalPages)
        assertEquals(1, result.totalElements)
        assertEquals(page, result.currentPage)
        assertEquals(size, result.pageSize)
    }

    @Test
    fun `FIND_PAGE_BOOK_CONTENT_BY_BOOK_ID`() = runTest {
        val size = 10
        val page = 1

        val book = BookTestFactory.createTestBook()

        val content = BookTestFactory.createTestBookContent()

        val pageResponse = PageResponse(
            result = listOf(content),
            totalPages = 1,
            totalElements = 1,
            currentPage = page,
            pageSize = size
        )

        coEvery {
            twoLevelCacheManager.getOrLoad(
                any(),
                any(),
                BookPageResponse.BookContentPagination::class.java,
                any()
            )
        } coAnswers {
            val loader = args[3] as (suspend () -> BookPageResponse.BookContentPagination)
            loader()
        }

        coEvery { bookContentFinder.findPageByBookId(book.id!!, size, page) } returns pageResponse

        val result = bookQueryService.findPageBookContentByBookId(book.id!!, size, page)

        assertEquals(1, result.bookContentInfos.size)
        assertEquals(content.id, result.bookContentInfos[0].bookContentId)
        assertEquals(content.content, result.bookContentInfos[0].content)
        assertEquals(1, result.totalPages)
        assertEquals(1, result.totalElements)
        assertEquals(page, result.currentPage)
        assertEquals(size, result.pageSize)
    }
}