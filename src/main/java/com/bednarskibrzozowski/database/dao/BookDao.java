package com.bednarskibrzozowski.database.dao;


import com.bednarskibrzozowski.database.domain.entities.BookEntity;

import java.util.List;
import java.util.Optional;

public interface BookDao {
    Optional<BookEntity> create(BookEntity book);

    Optional<BookEntity> findOne(String isbn);

    List<BookEntity> find();

    Optional<BookEntity> update(String isbn, BookEntity book);

    Optional<BookEntity> delete(String isbn);

    boolean isExists(String isbn);
}
