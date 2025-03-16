package com.library.infrastructure.repository.book

import com.library.app.book.model.BookContent
import com.library.infrastructure.dao.repository.book.content.BookContentCoroutineRepository
import com.library.infrastructure.dao.repository.book.content.BookContentEntity
import com.library.infrastructure.dao.repository.book.content.BookContentEntityRepository
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
class BookContentEntityRepositoryTest(
    @Autowired
    private val bookContentCoroutineRepository: BookContentCoroutineRepository
) {

    private val bookContentEntityRepository = BookContentEntityRepository(bookContentCoroutineRepository)

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
    fun GET_ALL_BY_BOOK_ID() = runTest {
        val genres = bookContentEntityRepository.getAllByBookId(1)

        assertEquals(3, genres.size)
    }

    @Test
    fun FIND_PAGE_BY_BOOK_ID() = runTest {
        val genres = bookContentEntityRepository.findPageByBookId(1, 3, 1)

        assertEquals(3, genres.totalElements)
        assertEquals(3, genres.pageSize)
        assertEquals(1, genres.currentPage)
        assertEquals(1, genres.totalPages)
    }

    @Test
    fun SAVE() = runTest {
        val bookContent = BookContent(
            content = "c1",
            bookId = 1
        )

        val savedContent = bookContentEntityRepository.save(bookContent)

        assertEquals("c1", savedContent.content)
        assertEquals(4, savedContent.id)
        assertEquals(1, savedContent.bookId)
    }
}