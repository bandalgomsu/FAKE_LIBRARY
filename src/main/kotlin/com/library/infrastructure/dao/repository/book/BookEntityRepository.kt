package com.library.infrastructure.dao.repository.book

import com.library.app.book.dao.BookDao
import com.library.app.book.model.Book

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
                userId = it.userId
            )
        }
    }
}