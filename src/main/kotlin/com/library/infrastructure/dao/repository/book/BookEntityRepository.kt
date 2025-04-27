package com.library.infrastructure.dao.repository.book

import com.library.app.book.dao.BookDao
import com.library.app.book.model.Book
import com.library.app.common.PageResponse
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class BookEntityRepository(
    private val bookRepository: BookCoroutineRepository,
    private val bookRowRepository: BookRowRepository,

    ) : BookDao {
    override suspend fun getById(bookId: Long): Book? {
        return bookRepository.findById(bookId)?.let {
            return@let it.toModel()
        }
    }

    override suspend fun findPage(size: Int, page: Int): PageResponse<Book> {
        val offset = (page - 1) * size
        val totalElements = bookRepository.count()

        val books = bookRepository.findPage(size, offset)
            .map { it.toModel() }
            .toList()

        val totalPages = (totalElements / size) + if (totalElements % size > 0) 1 else 0

        return PageResponse(
            result = books,
            totalPages = totalPages,
            totalElements = totalElements,
            currentPage = page,
            pageSize = size
        )
    }

    override suspend fun findPageByCreatedAtBetween(
        size: Int,
        page: Int,
        startDate: LocalDateTime,
        endData: LocalDateTime
    ): PageResponse<Book> {
        val offset = (page - 1) * size
        val totalElements = bookRepository.countByCreatedAtBetween(startDate, endData)

        val books = bookRepository.findPageByCreatedAtRange(startDate, endData, size, offset)
            .map {
                it.toModel()
            }.toList()

        val totalPages = (totalElements / size) + if (totalElements % size > 0) 1 else 0

        return PageResponse(
            result = books,
            totalPages = totalPages,
            totalElements = totalElements,
            currentPage = page,
            pageSize = size
        )
    }

    override suspend fun save(book: Book): Book {
        return bookRepository.save(
            BookEntity(
                plot = book.plot,
                title = book.title,
                userId = book.userId,
            )
        ).toModel()
    }
}