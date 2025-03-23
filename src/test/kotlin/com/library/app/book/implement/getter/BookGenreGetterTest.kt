package com.library.app.book.implement.getter

import com.library.app.book.BookTestFactory
import com.library.app.book.dao.BookGenreDao
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class BookGenreGetterTest {
    private lateinit var bookGenreDao: BookGenreDao

    private lateinit var bookGenreGetter: BookGenreGetter

    @BeforeEach
    fun setup() = runTest {
        bookGenreDao = mockk(relaxed = true)

        bookGenreGetter = BookGenreGetter(bookGenreDao)
    }

    @Test
    fun `GET_ALL_BY_BOOK_ID`() = runTest {
        val genre = BookTestFactory.createTestBookGenre()

        val genres = listOf(genre)

        coEvery { bookGenreDao.getAllByBookId(BookTestFactory.bookId) } returns genres

        val result = bookGenreGetter.getAllByBookId(BookTestFactory.bookId)

        assertEquals(genre, result[0])
        assertEquals(1, result.size)
    }
}