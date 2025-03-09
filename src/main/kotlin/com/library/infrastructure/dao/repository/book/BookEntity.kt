package com.library.infrastructure.dao.repository.book

import com.library.infrastructure.dao.repository.BaseEntity
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("book")
class BookEntity(
    @Id
    var id: Long? = null,
    @Column("plot")
    var plot: String,
    @Column("title")
    var title: String,
    @Column("user_id")
    var userId: Long? = null

) : BaseEntity() {


}