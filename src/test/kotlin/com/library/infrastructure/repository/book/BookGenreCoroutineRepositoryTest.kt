package com.library.infrastructure.repository.book

import com.library.infrastructure.dao.repository.book.genre.BookGenreCoroutineRepository
import com.library.infrastructure.dao.repository.book.genre.BookGenreEntity
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
class BookGenreCoroutineRepositoryTest(
    @Autowired
    private val bookGenreCoroutineRepository: BookGenreCoroutineRepository
) {
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
    fun FIND_ALL_BY_BOOK_ID() = runTest {
        val genres = bookGenreCoroutineRepository.findAllByBookId(1).toList()

        assertEquals(3, genres.size)
    }
}