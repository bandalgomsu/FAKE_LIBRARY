CREATE TABLE IF NOT EXISTS book(
    id bigint not null AUTO_INCREMENT,
    title varchar(50) not null ,
    plot varchar(100) ,
    user_id bigint ,
    created_at timestamp(6) ,
    updated_at timestamp(6) ,
    primary key (id)
);

CREATE TABLE IF NOT EXISTS book_page(
    id bigint not null AUTO_INCREMENT,
    contents varchar(1000) ,
    book_id bigint not null ,
    created_at timestamp(6) ,
    updated_at timestamp(6) ,
    primary key (id)
);

CREATE TABLE IF NOT EXISTS book_genre(
    id bigint not null AUTO_INCREMENT,
    genre varchar(20) not null,
    book_id bigint not null,
    created_at timestamp(6) ,
    updated_at timestamp(6) ,

    primary key (id)
);

CREATE TABLE IF NOT EXISTS book_comment(
    id bigint not null AUTO_INCREMENT ,
    comment varchar(200) ,
    book_id bigint not null ,
    user_id bigint not null ,
    created_at timestamp(6) ,
    updated_at timestamp(6) ,
    primary key (id)
);

CREATE TABLE IF NOT EXISTS book_favorite(
    id bigint not null AUTO_INCREMENT ,
    book_id bigint not null ,
    user_id bigint not null ,
    created_at timestamp(6) ,
    updated_at timestamp(6) ,
    primary key (id)
);

CREATE TABLE IF NOT EXISTS users(
    id bigint not null AUTO_INCREMENT,
    nickname varchar(20) not null,
    email varchar(50) not null,
    password varchar(50) not null,
    role Enum('USER','GUEST','ADMIN') not null,
    created_at timestamp(6) ,
    updated_at timestamp(6) ,
    primary key (id)
);
