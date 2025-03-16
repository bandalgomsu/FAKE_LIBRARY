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

@ActiveProfiles("test") // application-test.yml을 사용하도록 설정
@DataR2dbcTest
class BookCoroutineRepositoryTest {

    @Autowired
    private lateinit var bookCoroutineRepository: BookCoroutineRepository

    private val localDateTime = LocalDateTime.of(2025, 3, 3, 3, 3)

    @BeforeEach
    fun 초기화() = runTest {
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
    fun 리셋() = runTest {
        bookCoroutineRepository.deleteAll()
    }

    @Test
    fun 책_전체_조회_페이지네이션() = runTest {
        val books = bookCoroutineRepository.findPage(3, 0).toList()

        assertEquals(3, books.size)
    }

    @Test
    fun 책_날짜_범위_조회_페이지네이션() = runTest {
        val books = bookCoroutineRepository.findPageByCreatedAtRange(
            localDateTime.minusDays(1),
            localDateTime.plusDays(1),
            3,
            0
        ).toList()

        assertEquals(3, books.size)
    }

    @Test
    fun 책_날짜_범위_개수_조회_페이지네이션() = runTest {
        val count = bookCoroutineRepository.countByCreatedAtBetween(
            localDateTime.minusDays(1),
            localDateTime.plusDays(1)
        )

        assertEquals(3, count)
    }
}