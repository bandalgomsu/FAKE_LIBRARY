package com.library.infrastructure.dao.repository.book.genre

import com.library.app.book.dao.BookGenreDao
import com.library.app.book.model.BookGenre
import com.library.app.common.PageResponse
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository

@Repository
class BookGenreEntityRepository(
    private val bookGenreRepository: BookGenreCoroutineRepository,
    private val bookGenreRowRepository: BookGenreRowRepository,
    private val databaseClient: DatabaseClient
) : BookGenreDao {

    override suspend fun getAllByBookId(bookId: Long): List<BookGenre> {
        return bookGenreRepository.findAllByBookId(bookId)
            .map {
                return@map it.toModel()
            }.toList()
    }

    override suspend fun getAllInBookIds(bookIds: List<Long>): List<BookGenre> {
        return bookGenreRepository.findAllByBookIdIn(bookIds)
            .map {
                return@map it.toModel()
            }.toList()
    }

    override suspend fun findPage(size: Int, page: Int, genres: List<String>): PageResponse<BookGenre> {
        return bookGenreRowRepository.findPage(size, page, genres)
    }

    override suspend fun save(bookGenre: BookGenre): BookGenre {
        return bookGenreRepository.save(
            BookGenreEntity(
                genre = bookGenre.genre,
                bookId = bookGenre.bookId
            )
        ).let {
            BookGenre(
                id = it.id,
                genre = it.genre,
                bookId = it.bookId,
                createdAt = it.createdAt,
                updatedAt = it.updatedAt
            )
        }
    }
}