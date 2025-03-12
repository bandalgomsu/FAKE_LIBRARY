package com.library.app.book.dto

class BookRequest {

    data class BookAppendRequest(
        val title: String,
        val plot: String,

        val genre: String,

        val contents: String,
    )
}