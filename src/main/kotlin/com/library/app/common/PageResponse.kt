package com.library.app.common

class PageResponse<T>(
    val result: List<T>,
    val totalPages: Long,
    val totalElements: Long,
    val currentPage: Int,
    val pageSize: Int
) {
}