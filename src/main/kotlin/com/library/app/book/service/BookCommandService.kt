package com.library.app.book.service

import com.library.app.book.dto.BookRequest
import com.library.app.book.implement.saver.BookContentSaver
import com.library.app.book.implement.saver.BookGenreSaver
import com.library.app.book.implement.saver.BookSaver
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BookCommandService(
    private val bookGenreSaver: BookGenreSaver,
    private val bookContentSaver: BookContentSaver,
    private val bookSaver: BookSaver,
) {
    @Transactional
    suspend fun appendBook(request: BookRequest.BookAppendRequest) {
        val book = bookSaver.save(request.plot, request.title)

        coroutineScope {
            val page = async { bookContentSaver.save(request.contents, book.id!!) }
            val genre = async { bookGenreSaver.save(request.genre, book.id!!) }

            awaitAll(page, genre)
        }
    }
}