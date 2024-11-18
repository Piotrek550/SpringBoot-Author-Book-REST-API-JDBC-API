package com.devtiro.database.services.impl;

import com.devtiro.database.dao.BookDao;
import com.devtiro.database.domain.entities.BookEntity;
import com.devtiro.database.services.BookService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class BookServiceImpl implements BookService {

    private BookDao bookDao;

    public BookServiceImpl(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    @Override
    public BookEntity createBook(String isbn, BookEntity book) {
        book.setIsbn(isbn);
        bookDao.create(book);
        return book;
    }

    @Override
    public List<BookEntity> findAll() {
        return StreamSupport.stream(bookDao
                .find()
                .spliterator(),
                false)
                .toList();
    }

    @Override
    public Optional<BookEntity> findOne(String isbn) {
        return bookDao.findOne(isbn);
    }
}
