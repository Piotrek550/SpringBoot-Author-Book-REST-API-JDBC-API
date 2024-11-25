package com.bednarskibrzozowski.database.controllers;

import com.bednarskibrzozowski.database.domain.entities.BookEntity;
import com.bednarskibrzozowski.database.domain.dto.BookDto;
import com.bednarskibrzozowski.database.mappers.Mapper;
import com.bednarskibrzozowski.database.services.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class BookController {

    private Mapper<BookEntity, BookDto> bookMapper;

    private BookService bookService;

    public BookController(Mapper<BookEntity, BookDto> bookMapper, BookService bookService) {
        this.bookMapper = bookMapper;
        this.bookService = bookService;
    }

    /*@PutMapping(path = "/books/{isbn}")
    public ResponseEntity<BookDto> createBook(
            @PathVariable("isbn") String isbn,
            @RequestBody BookDto bookDto) {
        BookEntity bookEntity = bookMapper.mapFrom(bookDto);
        if(!bookService.isExists(isbn)) {
            Optional<BookEntity> savedBookEntity = bookService.createBook(isbn, bookEntity);
            return savedBookEntity.map(returnedBookEntity -> {
                BookDto createdBookDto = bookMapper.mapTo(returnedBookEntity);
                return new ResponseEntity<>(createdBookDto,HttpStatus.CREATED);
            }).orElse(
                    new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE)
            );
        }
        else {
            Optional<BookEntity> updatedBookEntity = bookService.update(isbn, bookEntity);
            return updatedBookEntity.map(returnedBookEntity -> {
                BookDto updatedBookDto = bookMapper.mapTo(returnedBookEntity);
                return new ResponseEntity<>(updatedBookDto,HttpStatus.OK);
            }).orElse(
                    new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE)
            );
        }
    }*/

    @GetMapping(path = "/books")
    public List<BookDto> listBooks() {
        List<BookEntity> books = bookService.findAll();
        return books.stream().map(bookMapper::mapTo).toList();
    }

    @GetMapping(path = "/books/{isbn}")
    public ResponseEntity<BookDto> getBook(@PathVariable("isbn") String isbn) {
        Optional<BookEntity> foundBook = bookService.findOne(isbn);
        return foundBook.map(bookEntity -> {
            BookDto bookDto = bookMapper.mapTo(bookEntity);
            return new ResponseEntity<>(bookDto, HttpStatus.OK);
        }).orElse(
                new ResponseEntity<>(HttpStatus.NOT_FOUND)
        );
    }

    @PatchMapping(path = "/books/{isbn}")
    public ResponseEntity<BookDto> partialUpdateBook(
            @PathVariable("isbn") String isbn,
            @RequestBody BookDto bookDto
    )
    {
        if(!bookService.isExists(isbn))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        BookEntity bookToPartialUpdate = bookMapper.mapFrom(bookDto);
        Optional<BookEntity> partialUpdatedBook = bookService.partialUpdate(isbn,bookToPartialUpdate);
        return partialUpdatedBook.map(partialUpdatedBookDto -> {
            BookDto toReturn = bookMapper.mapTo(partialUpdatedBookDto);
            return new ResponseEntity<>(toReturn, HttpStatus.OK);
        }).orElse(
                new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE)
        );
    }

    @DeleteMapping(path = "/books/{isbn}")
    public ResponseEntity<BookDto> deleteBook(
            @PathVariable("isbn") String isbn
    )
    {
        Optional<BookEntity> deletedBookEntity = bookService.delete(isbn);
        return deletedBookEntity.map(deletedBook -> {
            BookDto deletedBookDto = bookMapper.mapTo(deletedBook);
            return new ResponseEntity<>(deletedBookDto, HttpStatus.OK);
        }).orElse(
                new ResponseEntity<>(HttpStatus.NOT_FOUND)
        );
    }
}
