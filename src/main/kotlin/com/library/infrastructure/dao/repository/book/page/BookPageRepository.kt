package com.library.infrastructure.dao.repository.book.page

import com.library.app.book.dao.BookPageDao
import com.library.app.book.model.BookPage
import com.library.app.common.PageResponse
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Repository

@Repository
class BookPageRepository(
    private val bookPageRepository: BookPageCoroutineRepository
) : BookPageDao {

    override suspend fun getAllByBookId(bookId: Long): List<BookPage> {
        return bookPageRepository.findAllByBookId(bookId)
            .map {
                return@map BookPage(
                    id = it.id,
                    contents = it.contents,
                    bookId = it.bookId,
                    createdAt = it.createdAt,
                    updatedAt = it.updatedAt
                )
            }.toList()
    }

    override suspend fun findPageByBookId(bookId: Long, page: Int, size: Int): PageResponse<BookPage> {
        val offset = (page - 1) * size
        val totalElements = bookPageRepository.countByBookId(bookId)
        val bookPages = bookPageRepository.findPageByBookId(bookId, size, offset)
            .map {
                BookPage(
                    id = it.id,
                    contents = it.contents,
                    bookId = it.bookId,
                    createdAt = it.createdAt,
                    updatedAt = it.updatedAt
                )
            }.toList()

        val totalPages = (totalElements / size) + if (totalElements % size > 0) 1 else 0

        return PageResponse(
            result = bookPages,
            totalPages = totalPages,
            totalElements = totalElements,
            currentPage = page,
            pageSize = size
        )
    }

    override suspend fun save(bookPage: BookPage): BookPage {
        return bookPageRepository.save(
            BookPageEntity(
                contents = bookPage.contents,
                bookId = bookPage.bookId
            )
        ).let {
            BookPage(
                id = it.id,
                contents = it.contents,
                bookId = it.bookId,
                createdAt = it.createdAt,
                updatedAt = it.updatedAt
            )
        }
    }
}