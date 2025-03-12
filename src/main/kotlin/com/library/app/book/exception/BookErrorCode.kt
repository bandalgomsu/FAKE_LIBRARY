package com.library.app.book.exception

import com.library.app.common.exception.ErrorCode
import org.springframework.http.HttpStatus

enum class BookErrorCode(val code: String, val message: String, var status: Int) : ErrorCode {

    BOOK_NOT_FOUND("B01", "BOOK_NOT_FOUND", HttpStatus.BAD_REQUEST.value()),
    ;

    override fun getCodeValue(): String {
        return this.code
    }

    override fun getStatusValue(): Int {
        return this.status
    }

    override fun getMessageValue(): String {
        return this.message
    }
}