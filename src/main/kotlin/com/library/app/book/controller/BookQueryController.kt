package com.library.app.book.controller

import com.library.app.book.dto.BookPageResponse
import com.library.app.book.service.BookQueryService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/api/v1/books")
@RestController
class BookQueryController(
    private val bookQueryService: BookQueryService,
) {

    @GetMapping("")
    suspend fun findPageBook(
        @RequestParam size: Int = 10,
        @RequestParam page: Int = 1,
    ): ResponseEntity<BookPageResponse.BookInfoPagination> {
        val response = bookQueryService.findPageBook(size, page)

        return ResponseEntity.ok(response)
    }

    @GetMapping("/newBooks")
    suspend fun findPageNewBook(
        @RequestParam size: Int = 1,
        @RequestParam page: Int = 1,
    ): ResponseEntity<BookPageResponse.BookInfoPagination> {
        val response = bookQueryService.findPageNewBook(size, page)

        return ResponseEntity.ok(response)
    }

    @GetMapping("/{bookId}")
    suspend fun findPageBookContentsByBookId(
        @PathVariable bookId: Long,
        @RequestParam size: Int = 1,
        @RequestParam page: Int = 1,
    ): ResponseEntity<BookPageResponse.BookContentsPagination> {
        val response = bookQueryService.findPageBookContentsByBookId(bookId, size, page)

        return ResponseEntity.ok(response)
    }

    @GetMapping("/search/genre")
    suspend fun findPageBookByGenres(
        @RequestParam genres: List<String>,
        @RequestParam size: Int = 1,
        @RequestParam page: Int = 1,
    ): ResponseEntity<BookPageResponse.BookInfoPagination> {
        val response = bookQueryService.findPageBookByGenres(size, page, genres)

        return ResponseEntity.ok(response)
    }
}