package com.library.app.book.service

import com.library.app.book.implement.getter.BookGenreGetter
import com.library.app.book.implement.getter.BookGetter
import com.library.app.book.implement.getter.BookPageGetter
import org.springframework.stereotype.Service

@Service
class BookQueryService(
    private val bookGetter: BookGetter,
    private val bookPageGetter: BookPageGetter,
    private val bookGenreGetter: BookGenreGetter,
) {


}