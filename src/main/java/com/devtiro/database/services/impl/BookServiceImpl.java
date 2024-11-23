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
    public Optional<BookEntity> createBook(String isbn, BookEntity book) {
        book.setIsbn(isbn);
        return bookDao.create(book);
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

    @Override
    public Optional<BookEntity> update(String isbn, BookEntity bookEntity) {
        bookEntity.setIsbn(isbn);
        return bookDao.update(isbn, bookEntity);
    }

    @Override
    public Optional<BookEntity> partialUpdate(String isbn, BookEntity bookEntity) {
        bookEntity.setIsbn(isbn);
        return bookDao.findOne(isbn).map(existingBook -> {
            Optional.ofNullable(bookEntity.getIsbn()).ifPresent(existingBook::setIsbn);
            Optional.ofNullable(bookEntity.getTitle()).ifPresent(existingBook::setTitle);
            Optional.ofNullable(bookEntity.getAuthorId()).ifPresent(existingBook::setAuthorId);
            return bookDao.update(isbn,existingBook);
        }).orElseThrow(
                () -> new RuntimeException("Ksiazka nie istnieje!")
        );
    }

    @Override
    public Optional<BookEntity> delete(String isbn) {
        return bookDao.delete(isbn);
    }

    @Override
    public boolean isExists(String isbn) {
        return bookDao.isExists(isbn);
    }
}
