package com.devtiro.database.dao;


import com.devtiro.database.domain.entities.BookEntity;

import java.util.List;
import java.util.Optional;

public interface BookDao {
    void create(BookEntity book);

    Optional<BookEntity> findOne(String isbn);

    List<BookEntity> find();

    void update(String isbn, BookEntity book);

    void delete(String isbn);
}
