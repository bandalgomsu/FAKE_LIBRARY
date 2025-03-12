package com.library.infrastructure.dao.repository.book.genre

import com.library.infrastructure.dao.repository.BaseEntity
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("book_genre")
class BookGenreEntity(
    @Id
    var id: Long? = null,
    @Column("genre")
    var genre: String,
    @Column("book_id")
    var bookId: Long
) : BaseEntity() {
}