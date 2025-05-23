package com.library.infrastructure.dao.repository.book.content

import com.library.app.book.dao.BookContentDao
import com.library.app.book.model.BookContent
import com.library.app.common.PageResponse
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Repository

@Repository
class BookContentEntityRepository(
    private val bookContentRepository: BookContentCoroutineRepository
) : BookContentDao {

    override suspend fun getAllByBookId(bookId: Long): List<BookContent> {
        return bookContentRepository.findAllByBookId(bookId)
            .map { return@map it.toModel() }
            .toList()
    }

    override suspend fun findPageByBookId(bookId: Long, size: Int, page: Int): PageResponse<BookContent> {
        val offset = (page - 1) * size
        val totalElements = bookContentRepository.countByBookId(bookId)
        val bookContents = bookContentRepository.findPageByBookId(bookId, size, offset)
            .map { it.toModel() }
            .toList()

        val totalPages = (totalElements / size) + if (totalElements % size > 0) 1 else 0

        return PageResponse(
            result = bookContents,
            totalPages = totalPages,
            totalElements = totalElements,
            currentPage = page,
            pageSize = size
        )
    }

    override suspend fun save(bookContent: BookContent): BookContent {
        return bookContentRepository.save(
            BookContentEntity(
                content = bookContent.content,
                bookId = bookContent.bookId
            )
        ).toModel()
    }
}