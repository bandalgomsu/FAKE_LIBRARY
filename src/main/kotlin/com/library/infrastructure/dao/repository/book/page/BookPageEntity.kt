package com.library.infrastructure.dao.repository.book.page

import com.library.infrastructure.dao.repository.BaseEntity
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("book_page")
class BookPageEntity(
    @Id
    var id: Long? = null,
    @Column("contents")
    var contents: String,
    @Column("book_id")
    var bookId: Long
) : BaseEntity() {
}