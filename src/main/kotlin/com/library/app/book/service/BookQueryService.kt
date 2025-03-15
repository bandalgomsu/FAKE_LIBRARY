package com.library.app.book.service

import com.library.app.book.dto.BookPageResponse
import com.library.app.book.dto.BookResponse
import com.library.app.book.implement.finder.BookContentFinder
import com.library.app.book.implement.finder.BookFinder
import com.library.app.book.implement.getter.BookContentGetter
import com.library.app.book.implement.getter.BookGenreGetter
import com.library.app.book.implement.getter.BookGetter
import com.library.app.common.cache.CacheType
import com.library.app.common.cache.TwoLevelCacheManager
import kotlinx.coroutines.coroutineScope
import org.springframework.stereotype.Service

@Service
class BookQueryService(
    private val bookFinder: BookFinder,
    private val bookContentFinder: BookContentFinder,

    private val bookGetter: BookGetter,
    private val bookContentGetter: BookContentGetter,
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
                genres = it.genres,
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
        twoLevelCacheManager.getOrPut(
            CacheType.NEW_BOOK,
            "$page$size",
            BookPageResponse.BookInfoPagination::class.java,
            CacheType.NEW_BOOK.redisExpireSeconds
        ) {
            val bookPage = bookFinder.findPage(size, page)

            val books = bookPage.result.map {
                bookGenreGetter.getAllByBookId(bookId = it.id!!).map {
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

            return@getOrPut BookPageResponse.BookInfoPagination(
                bookInfos = books,
                totalPages = bookPage.totalPages,
                totalElements = bookPage.totalElements,
                currentPage = bookPage.currentPage,
                pageSize = bookPage.pageSize
            )
        }


    suspend fun findPageBookContentByBookId(
        bookId: Long,
        size: Int = 1,
        page: Int = 1
    ): BookPageResponse.BookContentPagination = twoLevelCacheManager.getOrPut(
        CacheType.BOOK_CONTENT,
        "$page$size",
        BookPageResponse.BookContentPagination::class.java,
        CacheType.NEW_BOOK.redisExpireSeconds
    ) {
        val bookPagePage = bookContentFinder.findPageBookPage(bookId, size, page)

        val bookContents = bookPagePage.result.map {
            BookResponse.BookContentInfo(
                bookPageId = it.id!!,
                content = it.content,
                createdAt = it.createdAt,
                updatedAt = it.updatedAt
            )
        }
        return@getOrPut BookPageResponse.BookContentPagination(
            bookContentInfos = bookContents,
            totalPages = bookPagePage.totalPages,
            totalElements = bookPagePage.totalElements,
            currentPage = bookPagePage.currentPage,
            pageSize = bookPagePage.pageSize
        )
    }
}