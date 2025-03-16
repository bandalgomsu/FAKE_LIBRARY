package com.library.infrastructure.dao.repository.book.genre

import com.library.app.book.dao.BookGenreDao
import com.library.app.book.model.BookGenre
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Repository

@Repository
class BookGenreEntityRepository(
    private val bookGenreRepository: BookGenreCoroutineRepository
) : BookGenreDao {

    override suspend fun getAllByBookId(bookId: Long): List<BookGenre> {
        return bookGenreRepository.findAllByBookId(bookId)
            .map {
                return@map BookGenre(
                    id = it.id,
                    genre = it.genre,
                    bookId = it.bookId,
                    createdAt = it.createdAt,
                    updatedAt = it.updatedAt
                )
            }.toList()
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