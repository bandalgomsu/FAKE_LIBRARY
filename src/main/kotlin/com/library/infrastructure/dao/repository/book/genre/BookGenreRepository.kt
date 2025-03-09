package com.library.infrastructure.dao.repository.book.genre

import com.library.app.book.dao.BookGenreDao

class BookGenreRepository(
    private val bookGenreRepository: BookGenreCoroutineRepository
) : BookGenreDao {
}