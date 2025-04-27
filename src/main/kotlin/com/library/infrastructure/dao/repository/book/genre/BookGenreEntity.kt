package com.library.infrastructure.dao.repository.book.genre

import com.library.app.book.model.BookGenre
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

    fun toModel(): BookGenre {
        return BookGenre(
            id = id,
            genre = genre,
            bookId = bookId,
            createdAt = super.createdAt,
            updatedAt = super.updatedAt
        )
    }
}