package com.library.infrastructure.repository.book

import com.library.app.book.model.BookGenre
import com.library.infrastructure.dao.repository.book.genre.BookGenreCoroutineRepository
import com.library.infrastructure.dao.repository.book.genre.BookGenreEntity
import com.library.infrastructure.dao.repository.book.genre.BookGenreEntityRepository
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest
import org.springframework.test.context.ActiveProfiles
import kotlin.test.assertEquals

@ActiveProfiles("test")
@DataR2dbcTest
class BookGenreEntityRepositoryTest(
    @Autowired
    private val bookGenreCoroutineRepository: BookGenreCoroutineRepository
) {

    private val bookGenreEntityRepository = BookGenreEntityRepository(bookGenreCoroutineRepository)

    @BeforeEach
    fun BEFORE_EACH() = runTest {
        val bookGenre1 = BookGenreEntity(null, "g1", 1)
        val bookGenre2 = BookGenreEntity(null, "g2", 1)
        val bookGenre3 = BookGenreEntity(null, "g3", 1)

        bookGenreCoroutineRepository.saveAll(listOf(bookGenre1, bookGenre2, bookGenre3)).toList()

        return@runTest
    }

    @AfterEach
    fun AFTER_EACH() = runTest {
        bookGenreCoroutineRepository.deleteAll()
    }

    @Test
    fun GET_ALL_BY_BOOK_ID() = runTest {
        val genres = bookGenreEntityRepository.getAllByBookId(1)

        assertEquals(3, genres.size)
    }

    @Test
    fun SAVE() = runTest {
        val genre = BookGenre(
            genre = "g1",
            bookId = 1
        )

        val savedGenre = bookGenreEntityRepository.save(genre)

        assertEquals("g1", savedGenre.genre)
        assertEquals(4, savedGenre.id)
        assertEquals(1, savedGenre.bookId)
    }

}