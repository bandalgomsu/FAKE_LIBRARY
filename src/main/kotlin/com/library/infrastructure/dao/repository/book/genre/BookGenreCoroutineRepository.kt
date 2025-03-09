package com.library.infrastructure.dao.repository.book.genre

import com.library.app.book.model.BookGenre
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface BookGenreCoroutineRepository : CoroutineCrudRepository<BookGenre, Long> {
}