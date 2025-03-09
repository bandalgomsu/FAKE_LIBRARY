package com.library.infrastructure.dao.repository.book.page

import com.library.app.book.model.BookPage
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface BookPageCoroutineRepository : CoroutineCrudRepository<BookPage, Long> {
}