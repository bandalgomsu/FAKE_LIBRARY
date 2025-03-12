package com.library.app.book.service

import com.library.app.book.dto.BookResponse
import com.library.app.book.implement.finder.BookFinder
import com.library.app.book.implement.finder.BookPageFinder
import com.library.app.book.implement.getter.BookGenreGetter
import com.library.app.book.implement.getter.BookGetter
import com.library.app.book.implement.getter.BookPageGetter
import com.library.app.common.PageResponse
import kotlinx.coroutines.coroutineScope
import org.springframework.stereotype.Service

@Service
class BookQueryService(
    private val bookFinder: BookFinder,
    private val bookPageFinder: BookPageFinder,

    private val bookGetter: BookGetter,
    private val bookPageGetter: BookPageGetter,
    private val bookGenreGetter: BookGenreGetter,
) {

    suspend fun findPageBook(size: Int = 1, page: Int = 1): PageResponse<BookResponse.BookInfo> = coroutineScope {
        val bookPages = bookFinder.findPage(size, page)

        val books = bookPages.result.map {
            val genres = bookGenreGetter.getAllByBookId(bookId = it.id!!).map {
                it.genre
            }.toList()

            BookResponse.BookInfo(
                bookId = it.id,
                title = it.title,
                plot = it.plot,
                genres = it.genres,
                createdAt = it.createdAt,
                updatedAt = it.updatedAt
            )
        }.toList()

        return@coroutineScope PageResponse(
            result = books,
            totalPages = bookPages.totalPages,
            totalElements = bookPages.totalElements,
            currentPage = bookPages.currentPage,
            pageSize = bookPages.pageSize
        )
    }

    suspend fun findPageNewBook(size: Int = 1, page: Int = 1): PageResponse<BookResponse.BookInfo> = coroutineScope {
        val bookPage = bookFinder.findPage(size, page)

        val books = bookPage.result.map {
            val genres = bookGenreGetter.getAllByBookId(bookId = it.id!!).map {
                it.genre
            }.toList()

            BookResponse.BookInfo(
                bookId = it.id,
                title = it.title,
                plot = it.plot,
                genres = it.genres,
                createdAt = it.createdAt,
                updatedAt = it.updatedAt
            )
        }.toList()

        return@coroutineScope PageResponse(
            result = books,
            totalPages = bookPage.totalPages,
            totalElements = bookPage.totalElements,
            currentPage = bookPage.currentPage,
            pageSize = bookPage.pageSize
        )
    }

    suspend fun findPageBookPageByBookId(
        bookId: Long,
        size: Int = 1,
        page: Int = 1
    ): PageResponse<BookResponse.BookPageInfo> {
        val bookPagePage = bookPageFinder.findPageBookPage(bookId, size, page)

        val bookPages = bookPagePage.result.map {
            BookResponse.BookPageInfo(
                bookPageId = it.id!!,
                contents = it.contents,
                createdAt = it.createdAt,
                updatedAt = it.updatedAt
            )
        }
        return PageResponse(
            result = bookPages,
            totalPages = bookPagePage.totalPages,
            totalElements = bookPagePage.totalElements,
            currentPage = bookPagePage.currentPage,
            pageSize = bookPagePage.pageSize
        )
    }
}