package com.bednarskibrzozowski.database.services;


import com.bednarskibrzozowski.database.domain.entities.BookEntity;

import java.util.List;
import java.util.Optional;

public interface BookService {

    Optional<BookEntity> createBook(String isbn, BookEntity book);

    List<BookEntity> findAll();

    Optional<BookEntity> findOne(String isbn);

    boolean isExists(String isbn);

    Optional<BookEntity> update(String isbn, BookEntity bookEntity);

    Optional<BookEntity> delete(String isbn);

    Optional<BookEntity> partialUpdate(String isbn, BookEntity bookEntity);
}
