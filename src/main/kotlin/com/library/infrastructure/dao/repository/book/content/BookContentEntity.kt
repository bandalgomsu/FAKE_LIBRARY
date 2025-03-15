package com.library.infrastructure.dao.repository.book.content

import com.library.infrastructure.dao.repository.BaseEntity
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("book_content")
class BookContentEntity(
    @Id
    var id: Long? = null,
    @Column("content")
    var content: String,
    @Column("book_id")
    var bookId: Long
) : BaseEntity() {
}