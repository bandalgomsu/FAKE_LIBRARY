package com.library.infrastructure.repository.book

import com.library.infrastructure.dao.repository.book.BookCoroutineRepository
import com.library.infrastructure.dao.repository.book.BookEntity
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
class BookCoroutineRepositoryTest {

    @Autowired
    private lateinit var bookCoroutineRepository: BookCoroutineRepository

    private val localDateTime = LocalDateTime.of(2025, 3, 3, 3, 3)

    @BeforeEach
    fun BEFORE_EACH() = runTest {
        val book1 = BookEntity(null, "p1", "t1")
        book1.createdAt = localDateTime
        book1.updatedAt = localDateTime

        val book2 = BookEntity(null, "p2", "t2")
        book2.createdAt = localDateTime
        book2.updatedAt = localDateTime

        val book3 = BookEntity(null, "p3", "t3")
        book3.createdAt = localDateTime
        book3.updatedAt = localDateTime

        bookCoroutineRepository.saveAll(listOf(book1, book2, book3)).toList()

        return@runTest
    }

    @AfterEach
    fun AFTER_EACH() = runTest {
        bookCoroutineRepository.deleteAll()
    }

    @Test
    fun FIND_PAGE() = runTest {
        val books = bookCoroutineRepository.findPage(3, 0).toList()

        assertEquals(3, books.size)
    }

    @Test
    fun FIND_PAGE_BY_CREATED_AT_RANGE() = runTest {
        val books = bookCoroutineRepository.findPageByCreatedAtRange(
            localDateTime.minusDays(1),
            localDateTime.plusDays(1),
            3,
            0
        ).toList()

        assertEquals(3, books.size)
    }

    @Test
    fun COUNT_BY_CREATED_AT_RANGE() = runTest {
        val count = bookCoroutineRepository.countByCreatedAtBetween(
            localDateTime.minusDays(1),
            localDateTime.plusDays(1)
        )

        assertEquals(3, count)
    }
}