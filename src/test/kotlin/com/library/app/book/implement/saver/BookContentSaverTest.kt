package com.library.app.book.implement.saver

import com.library.app.book.BookTestFactory
import com.library.app.book.dao.BookContentDao
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class BookContentSaverTest {
    private lateinit var bookContentDao: BookContentDao

    private lateinit var bookContentSaver: BookContentSaver

    @BeforeEach
    fun setup() = runTest {
        bookContentDao = mockk(relaxed = true)

        bookContentSaver = BookContentSaver(bookContentDao)
    }

    @Test
    fun `SAVE`() = runTest {
        val content = BookTestFactory.createTestBookContent()

        coEvery { bookContentDao.save(any()) } returns content

        val result = bookContentSaver.save(BookTestFactory.content, BookTestFactory.bookId)

        assertEquals(content, result)
        assertEquals(content.id, result.id)
        assertEquals(content.content, result.content)
        assertEquals(content.bookId, result.bookId)
        assertEquals(content.createdAt, result.createdAt)
        assertEquals(content.updatedAt, result.updatedAt)
    }
}