package com.library.infrastructure.repository.book

import com.library.infrastructure.dao.repository.book.content.BookContentCoroutineRepository
import com.library.infrastructure.dao.repository.book.content.BookContentEntity
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime
import kotlin.test.assertEquals

@ActiveProfiles("test")
@DataR2dbcTest
class BookContentCoroutineRepositoryTest(
    @Autowired
    private val bookContentCoroutineRepository: BookContentCoroutineRepository
) {

    private val localDateTime = LocalDateTime.of(2025, 3, 3, 3, 3)

    @BeforeEach
    fun BEFORE_EACH() = runTest {
        val bookContent1 = BookContentEntity(null, "c1", 1)
        bookContent1.createdAt = localDateTime
        bookContent1.updatedAt = localDateTime

        val bookContent2 = BookContentEntity(null, "c2", 1)
        bookContent2.createdAt = localDateTime
        bookContent2.updatedAt = localDateTime

        val bookContent3 = BookContentEntity(null, "c3", 1)
        bookContent3.createdAt = localDateTime
        bookContent3.updatedAt = localDateTime

        bookContentCoroutineRepository.saveAll(listOf(bookContent1, bookContent2, bookContent3)).toList()

        return@runTest
    }

    @AfterEach
    fun AFTER_EACH() = runTest {
        bookContentCoroutineRepository.deleteAll()
    }

    @Test
    fun FIND_ALL_BY_BOOK_ID() = runTest {
        val genres = bookContentCoroutineRepository.findAllByBookId(1).toList()

        assertEquals(3, genres.size)
    }

    @Test
    fun COUNT_BY_BOOK_ID() = runTest {
        val count = bookContentCoroutineRepository.countByBookId(1)

        assertEquals(3, count)
    }

    @Test
    fun FIND_PAGE_BY_BOOK_ID() = runTest {
        val genres = bookContentCoroutineRepository.findPageByBookId(1, 3, 0).toList()

        assertEquals(3, genres.size)
    }
}