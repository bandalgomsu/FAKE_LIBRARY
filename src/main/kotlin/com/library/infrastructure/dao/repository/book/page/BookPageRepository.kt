package com.library.infrastructure.dao.repository.book.page

import com.library.app.book.dao.BookPageDao

class BookPageRepository(
    private val bookPageRepository: BookPageCoroutineRepository
) : BookPageDao {
}