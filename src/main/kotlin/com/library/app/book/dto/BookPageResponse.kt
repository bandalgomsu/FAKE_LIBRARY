package com.library.app.book.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

class BookPageResponse {
    class BookInfoPagination(
        @JsonProperty("bookInfos") val bookInfos: List<BookResponse.BookInfo>,
        @JsonProperty("totalPages") val totalPages: Long,
        @JsonProperty("totalElements") val totalElements: Long,
        @JsonProperty("currentPage") val currentPage: Int,
        @JsonProperty("pageSize") val pageSize: Int,
    ) : Serializable {
    }

    class BookContentsPagination(
        @JsonProperty("bookContentInfos") val bookContentInfos: List<BookResponse.BookContentInfo>,
        @JsonProperty("totalPages") val totalPages: Long,
        @JsonProperty("totalElements") val totalElements: Long,
        @JsonProperty("currentPage") val currentPage: Int,
        @JsonProperty("pageSize") val pageSize: Int,
    ) : Serializable {
    }
}