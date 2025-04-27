package com.library.app.book.service

import com.library.app.book.dto.BookPageResponse
import com.library.app.book.dto.BookResponse
import com.library.app.book.implement.finder.BookContentFinder
import com.library.app.book.implement.finder.BookFinder
import com.library.app.book.implement.finder.BookGenreFinder
import com.library.app.book.implement.getter.BookGenreGetter
import com.library.app.book.implement.getter.BookGetter
import com.library.app.common.cache.CacheType
import com.library.app.common.cache.TwoLevelCacheManager
import kotlinx.coroutines.coroutineScope
import org.springframework.stereotype.Service

@Service
class BookQueryService(
    private val bookFinder: BookFinder,
    private val bookGetter: BookGetter,

    private val bookContentFinder: BookContentFinder,

    private val bookGenreGetter: BookGenreGetter,
    private val bookGenreFinder: BookGenreFinder,

    private val twoLevelCacheManager: TwoLevelCacheManager
) {

    suspend fun findPageBook(size: Int = 10, page: Int = 1): BookPageResponse.BookInfoPagination = coroutineScope {
        val bookPage = bookFinder.findPage(size, page)

        val bookGenres = bookGenreGetter.getAllInBookIds(bookPage.result.map { it.id!! }.toList())

        val genreMap: Map<Long, List<String>> = bookGenres.groupBy { it.bookId }
            .mapValues { entry -> entry.value.map { it.genre } }

        val booksInfo = bookPage.result.map { book ->
            val genres = genreMap[book.id!!] ?: emptyList()

            BookResponse.BookInfo(
                bookId = book.id,
                title = book.title,
                plot = book.plot,
                genres = genres,
                createdAt = book.createdAt,
                updatedAt = book.updatedAt
            )
        }

        return@coroutineScope BookPageResponse.BookInfoPagination(
            bookInfos = booksInfo,
            totalPages = bookPage.totalPages,
            totalElements = bookPage.totalElements,
            currentPage = bookPage.currentPage,
            pageSize = bookPage.pageSize
        )
    }

    suspend fun findPageNewBook(size: Int = 10, page: Int = 1): BookPageResponse.BookInfoPagination =
        twoLevelCacheManager.getOrLoad(
            CacheType.NEW_BOOK,
            "$page$size",
            BookPageResponse.BookInfoPagination::class.java,
        ) {
            val newBookPage = bookFinder.findPageNewBook(size, page)

            val bookGenres = bookGenreGetter.getAllInBookIds(newBookPage.result.map { it.id!! }.toList())

            val genreMap: Map<Long, List<String>> = bookGenres.groupBy { it.bookId }
                .mapValues { entry -> entry.value.map { it.genre } }

            val newBooksInfo = newBookPage.result.map { book ->
                val genres = genreMap[book.id!!] ?: emptyList()

                BookResponse.BookInfo(
                    bookId = book.id,
                    title = book.title,
                    plot = book.plot,
                    genres = genres,
                    createdAt = book.createdAt,
                    updatedAt = book.updatedAt
                )
            }

            BookPageResponse.BookInfoPagination(
                bookInfos = newBooksInfo,
                totalPages = newBookPage.totalPages,
                totalElements = newBookPage.totalElements,
                currentPage = newBookPage.currentPage,
                pageSize = newBookPage.pageSize
            )
        }


    suspend fun findPageBookContentsByBookId(
        bookId: Long,
        size: Int = 1,
        page: Int = 1
    ): BookPageResponse.BookContentsPagination = twoLevelCacheManager.getOrLoad(
        CacheType.BOOK_CONTENT,
        "$bookId$page$size",
        BookPageResponse.BookContentsPagination::class.java,
    ) {
        val bookContentsPage = bookContentFinder.findPageByBookId(bookId, size, page)

        val bookContents = bookContentsPage.result.map {
            BookResponse.BookContentInfo(
                bookContentId = it.id!!,
                content = it.content,
                createdAt = it.createdAt,
                updatedAt = it.updatedAt
            )
        }
        return@getOrLoad BookPageResponse.BookContentsPagination(
            bookContentInfos = bookContents,
            totalPages = bookContentsPage.totalPages,
            totalElements = bookContentsPage.totalElements,
            currentPage = bookContentsPage.currentPage,
            pageSize = bookContentsPage.pageSize
        )
    }

    suspend fun findPageBookByGenres(
        size: Int = 10,
        page: Int = 1,
        genres: List<String>
    ): BookPageResponse.BookInfoPagination = coroutineScope {
        val genrePage = bookGenreFinder.findPage(size, page, genres)

        val booksInfo = genrePage.result
            .groupBy { it.bookId }
            .mapValues { entry ->
                entry.value.map { it.genre }
            }
            .map {
                val book = bookGetter.getBookById(it.key)

                BookResponse.BookInfo(
                    bookId = book.id!!,
                    title = book.title,
                    plot = book.plot,
                    genres = it.value,
                    createdAt = book.createdAt,
                    updatedAt = book.updatedAt
                )
            }

        return@coroutineScope BookPageResponse.BookInfoPagination(
            bookInfos = booksInfo,
            totalPages = genrePage.totalPages,
            totalElements = genrePage.totalElements,
            currentPage = genrePage.currentPage,
            pageSize = genrePage.pageSize
        )
    }
}