package com.library.app.book.service

import com.library.app.book.dto.BookPageResponse
import com.library.app.book.dto.BookResponse
import com.library.app.book.implement.finder.BookContentFinder
import com.library.app.book.implement.finder.BookFinder
import com.library.app.book.implement.getter.BookGenreGetter
import com.library.app.common.cache.CacheType
import com.library.app.common.cache.TwoLevelCacheManager
import kotlinx.coroutines.coroutineScope
import org.springframework.stereotype.Service

@Service
class BookQueryService(
    private val bookFinder: BookFinder,
    private val bookContentFinder: BookContentFinder,
    private val bookGenreGetter: BookGenreGetter,

    private val twoLevelCacheManager: TwoLevelCacheManager
) {

    suspend fun findPageBook(size: Int = 10, page: Int = 1): BookPageResponse.BookInfoPagination = coroutineScope {
        val bookPage = bookFinder.findPage(size, page)

        val books = bookPage.result.map {
            val genres = bookGenreGetter.getAllByBookId(bookId = it.id!!).map {
                it.genre
            }.toList()

            BookResponse.BookInfo(
                bookId = it.id,
                title = it.title,
                plot = it.plot,
                genres = genres,
                createdAt = it.createdAt,
                updatedAt = it.updatedAt
            )
        }.toList()

        return@coroutineScope BookPageResponse.BookInfoPagination(
            bookInfos = books,
            totalPages = bookPage.totalPages,
            totalElements = bookPage.totalElements,
            currentPage = bookPage.currentPage,
            pageSize = bookPage.pageSize
        )
    }

    suspend fun findPageNewBook(size: Int = 1, page: Int = 1): BookPageResponse.BookInfoPagination =
        twoLevelCacheManager.getOrLoad(
            CacheType.NEW_BOOK,
            "$page$size",
            BookPageResponse.BookInfoPagination::class.java,
        ) {
            val newBookPage = bookFinder.findPageNewBook(size, page)

            val newBooks = newBookPage.result.map {
                val genres = bookGenreGetter.getAllByBookId(bookId = it.id!!).map {
                    it.genre
                }.toList()

                BookResponse.BookInfo(
                    bookId = it.id,
                    title = it.title,
                    plot = it.plot,
                    genres = genres,
                    createdAt = it.createdAt,
                    updatedAt = it.updatedAt
                )
            }.toList()

            return@getOrLoad BookPageResponse.BookInfoPagination(
                bookInfos = newBooks,
                totalPages = newBookPage.totalPages,
                totalElements = newBookPage.totalElements,
                currentPage = newBookPage.currentPage,
                pageSize = newBookPage.pageSize
            )
        }


    suspend fun findPageBookContentByBookId(
        bookId: Long,
        size: Int = 1,
        page: Int = 1
    ): BookPageResponse.BookContentPagination = twoLevelCacheManager.getOrLoad(
        CacheType.BOOK_CONTENT,
        "$bookId$page$size",
        BookPageResponse.BookContentPagination::class.java,
    ) {
        val bookPagePage = bookContentFinder.findPageByBookId(bookId, size, page)

        val bookContents = bookPagePage.result.map {
            BookResponse.BookContentInfo(
                bookPageId = it.id!!,
                content = it.content,
                createdAt = it.createdAt,
                updatedAt = it.updatedAt
            )
        }
        return@getOrLoad BookPageResponse.BookContentPagination(
            bookContentInfos = bookContents,
            totalPages = bookPagePage.totalPages,
            totalElements = bookPagePage.totalElements,
            currentPage = bookPagePage.currentPage,
            pageSize = bookPagePage.pageSize
        )
    }
}