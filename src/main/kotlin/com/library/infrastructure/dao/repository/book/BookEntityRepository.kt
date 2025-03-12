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
    private val bookRepository: BookCoroutineRepository
) : BookDao {
    override suspend fun getById(bookId: Long): Book? {
        return bookRepository.findById(bookId)?.let {
            return@let Book(
                id = it.id,
                plot = it.plot,
                title = it.title,
                userId = it.userId
            )
        }
    }

    override suspend fun findPage(size: Int, page: Int): PageResponse<Book> {
        val offset = (page - 1) * size
        val totalElements = bookRepository.count()

        val books = bookRepository.findPage(size, offset)
            .map {
                Book(
                    id = it.id,
                    plot = it.plot,
                    title = it.title,
                    userId = it.userId,
                    createdAt = it.createdAt,
                    updatedAt = it.updatedAt
                )
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

    override suspend fun findPageByCreatedAtBetween(
        size: Int,
        page: Int,
        startDate: LocalDateTime,
        endData: LocalDateTime
    ): PageResponse<Book> {
        val offset = (page - 1) * size
        val totalElements = bookRepository.countByCreatedAtBetween(startDate, endData, size, offset)

        val books = bookRepository.findPageByCreatedAtRange(
            startDate, endData, size, offset
        )
            .map {
                Book(
                    id = it.id,
                    plot = it.plot,
                    title = it.title,
                    userId = it.userId,
                    createdAt = it.createdAt,
                    updatedAt = it.updatedAt
                )
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
                title = book.plot,
                userId = book.userId,
            )
        ).let {
            Book(
                id = it.id,
                plot = it.plot,
                title = it.title,
                userId = it.userId,
                createdAt = it.createdAt,
                updatedAt = it.updatedAt
            )
        }
    }
}