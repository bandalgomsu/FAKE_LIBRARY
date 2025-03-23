package com.library.app.book.service

import com.library.app.book.implement.saver.BookContentSaver
import com.library.app.book.implement.saver.BookGenreSaver
import com.library.app.book.implement.saver.BookSaver
import com.library.app.common.cache.CacheType
import com.library.app.common.cache.TwoLevelCacheManager
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
    private val bookContentSaver: BookContentSaver,

    private val twoLevelCacheManager: TwoLevelCacheManager,

    private val llmClient: LLMClient,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Scheduled(cron = "0 0 2 * * ?")
    suspend fun startCreateBookSchedule() {
        repeat(15 * 100) {
            try {
                val bookInfo = llmClient.createBookInfo()

                val savedBook = bookSaver.save(bookInfo.plot, bookInfo.title)

                coroutineScope {
                    val content = async { bookContentSaver.save(bookInfo.content, savedBook.id!!) }

                    val genres = async {
                        bookInfo.genres.forEach {
                            bookGenreSaver.save(it, savedBook.id!!)
                        }
                    }

                    awaitAll(content, genres)
                }

                logger.info("BOOK CREATE SUCCESS, i = {}", it)
            } catch (e: Exception) {
                logger.error(e.message)
                logger.error("BOOK CREATE FAILURE, i = {}", it)
            }

            delay(5000)
        }

        logger.info("CACHE_CLEAR")
        twoLevelCacheManager.clearAll(CacheType.NEW_BOOK)
    }
}