package com.library.app.book.service

import com.library.app.book.implement.saver.BookGenreSaver
import com.library.app.book.implement.saver.BookPageSaver
import com.library.app.book.implement.saver.BookSaver
import com.library.app.common.llm.LLMClient
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class BookScheduleService(
    private val bookSaver: BookSaver,
    private val bookGenreSaver: BookGenreSaver,
    private val bookPageSaver: BookPageSaver,

    private val llmClient: LLMClient,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Scheduled(cron = "0 33 20 * * ?")
    suspend fun startCreateBookSchedule() {
        repeat(15 * 100) {
            try {
                val bookInfo = llmClient.createBookInfo()

                val savedBook = bookSaver.save(bookInfo.plot, bookInfo.title)

                coroutineScope {
                    val page = async { bookPageSaver.save(bookInfo.contents, savedBook.id!!) }

                    val genres = async {
                        bookInfo.genres.forEach {
                            bookGenreSaver.save(it, savedBook.id!!)
                        }
                    }

                    awaitAll(page, genres)
                }

                logger.info("BOOK CREATE SUCCESS, i = {}", it)
            } catch (e: Exception) {
                logger.error(e.message)
                logger.error("BOOK CREATE FAILURE, i = {}", it)
            }

            delay(5000)
        }
    }
}