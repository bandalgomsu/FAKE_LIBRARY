package com.library.app.book.controller

import com.library.app.book.dto.BookResponse
import com.library.app.book.service.BookQueryService
import com.library.app.common.PageResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/api/v1/books")
@RestController
class BookQueryController(
    private val bookQueryService: BookQueryService,
) {

    @GetMapping("")
    suspend fun findPageBook(
        @RequestParam size: Int = 1,
        @RequestParam page: Int = 1,
    ): ResponseEntity<PageResponse<BookResponse.BookInfo>> {
        val response = bookQueryService.findPageBook(size, page)

        return ResponseEntity.ok(response)
    }

    @GetMapping("/newBooks")
    suspend fun findPageNewBook(
        @RequestParam size: Int = 1,
        @RequestParam page: Int = 1,
    ): ResponseEntity<BookResponse.BookInfoPagination> {
        val response = bookQueryService.findPageNewBook(size, page)

        return ResponseEntity.ok(response)
    }

    @GetMapping("/{bookId}")
    suspend fun findPageBookPageByBookId(
        @PathVariable bookId: Long,
        @RequestParam size: Int = 1,
        @RequestParam page: Int = 1,
    ): ResponseEntity<PageResponse<BookResponse.BookContentInfo>> {
        val response = bookQueryService.findPageBookPageByBookId(bookId, size, page)

        return ResponseEntity.ok(response)
    }
}