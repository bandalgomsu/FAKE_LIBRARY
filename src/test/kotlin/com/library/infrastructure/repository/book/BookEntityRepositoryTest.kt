package com.library.infrastructure.repository.book

import com.library.app.book.model.Book
import com.library.infrastructure.dao.repository.book.BookCoroutineRepository
import com.library.infrastructure.dao.repository.book.BookEntity
import com.library.infrastructure.dao.repository.book.BookEntityRepository
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
class BookEntityRepositoryTest(
    @Autowired
    private val bookCoroutineRepository: BookCoroutineRepository
) {

    private var bookEntityRepository: BookEntityRepository = BookEntityRepository(bookCoroutineRepository)

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
    fun GET_BY_ID() = runTest {
        val book = bookEntityRepository.getById(1)!!

        assertEquals(1, book.id)
        assertEquals("p1", book.plot)
        assertEquals("t1", book.title)
    }

    @Test
    fun FIND_PAGE() = runTest {
        val bookPage = bookEntityRepository.findPage(3, 1)

        assertEquals(3, bookPage.totalElements)
        assertEquals(3, bookPage.pageSize)
        assertEquals(1, bookPage.currentPage)
        assertEquals(1, bookPage.totalPages)
    }

    @Test
    fun FIND_PAGE_BY_CREATED_AT_BETWEEN() = runTest {
        val bookPage =
            bookEntityRepository.findPageByCreatedAtBetween(3, 1, localDateTime.minusDays(1), localDateTime.plusDays(1))

        assertEquals(3, bookPage.totalElements)
        assertEquals(3, bookPage.pageSize)
        assertEquals(1, bookPage.currentPage)
        assertEquals(1, bookPage.totalPages)
    }

    @Test
    fun SAVE() = runTest {
        val book = Book(
            plot = "p4",
            title = "t4"
        )

        val savedBook = bookEntityRepository.save(book)

        assertEquals("p4", savedBook.plot)
        assertEquals("t4", savedBook.title)
    }
}