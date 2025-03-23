package com.library.app.book.implement.saver

import com.library.app.book.BookTestFactory
import com.library.app.book.dao.BookDao
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class BookSaverTest {
    private lateinit var bookDao: BookDao

    private lateinit var bookSaver: BookSaver

    @BeforeEach
    fun setup() = runTest {
        bookDao = mockk(relaxed = true)

        bookSaver = BookSaver(bookDao)
    }

    @Test
    fun `SAVE`() = runTest {
        val book = BookTestFactory.createTestBook()

        coEvery { bookDao.save(any()) } returns book

        val result = bookSaver.save(BookTestFactory.plot, BookTestFactory.title)

        assertEquals(book, result)
        assertEquals(book.id, result.id)
        assertEquals(book.title, result.title)
        assertEquals(book.plot, result.plot)
        assertEquals(book.createdAt, result.createdAt)
        assertEquals(book.updatedAt, result.updatedAt)
    }
}