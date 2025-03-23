package com.library.app.book.implement.saver

import com.library.app.book.BookTestFactory
import com.library.app.book.dao.BookGenreDao
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class BookGenreSaverTest {
    private lateinit var bookGenreDao: BookGenreDao

    private lateinit var bookGenreSaver: BookGenreSaver

    @BeforeEach
    fun setup() = runTest {
        bookGenreDao = mockk(relaxed = true)

        bookGenreSaver = BookGenreSaver(bookGenreDao)
    }

    @Test
    fun `SAVE`() = runTest {
        val genre = BookTestFactory.createTestBookGenre()

        coEvery { bookGenreDao.save(any()) } returns genre

        val result = bookGenreSaver.save(BookTestFactory.genre, BookTestFactory.bookId)

        assertEquals(genre, result)
        assertEquals(genre.id, result.id)
        assertEquals(genre.genre, result.genre)
        assertEquals(genre.bookId, result.bookId)
        assertEquals(genre.createdAt, result.createdAt)
        assertEquals(genre.updatedAt, result.updatedAt)
    }
}