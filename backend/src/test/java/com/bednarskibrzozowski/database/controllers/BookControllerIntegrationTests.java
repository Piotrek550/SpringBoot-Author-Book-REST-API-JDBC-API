package com.bednarskibrzozowski.database.controllers;

import com.bednarskibrzozowski.database.TestDataUtil;
import com.bednarskibrzozowski.database.domain.dto.AuthorDto;
import com.bednarskibrzozowski.database.domain.dto.BookDto;
import com.bednarskibrzozowski.database.domain.entities.AuthorEntity;
import com.bednarskibrzozowski.database.domain.entities.BookEntity;
import com.bednarskibrzozowski.database.services.AuthorService;
import com.bednarskibrzozowski.database.services.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class BookControllerIntegrationTests {

    private AuthorService authorService;

    private BookService bookService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Autowired
    public BookControllerIntegrationTests(MockMvc mockMvc, ObjectMapper objectMapper, BookService bookService, AuthorService authorService) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.bookService = bookService;
        this.authorService = authorService;
    }


    @Test
    public void testThatCreateBookReturnsHttpStatus201Created() throws Exception {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorEntityA();
        AuthorDto authorDto = TestDataUtil.createTestAuthorDtoA();
        authorService.createAuthor(authorEntity);
        BookDto bookDto = BookDto.builder()
                .isbn("toJestIsbn")
                .title("Dzieci z Bulerbyn")
                .author(authorDto)
                .build();
        String createBookJson = objectMapper.writeValueAsString(bookDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/" + bookDto.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBookJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateBookReturnsCreatedBook() throws Exception {
        AuthorDto authorDto = TestDataUtil.createTestAuthorDtoA();
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorEntityA();
        authorService.createAuthor(authorEntity);
        BookDto bookDto = TestDataUtil.createTestBookDtoA(authorDto);
        String createBookJson = objectMapper.writeValueAsString(bookDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/" + bookDto.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBookJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(bookDto.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(bookDto.getTitle())
        );
    }

    @Test
    public void testThatCreateBookReturnsHttpStatus200Ok() throws Exception {
        BookDto bookDto = TestDataUtil.createTestBookDtoA(null);
        String createBookJson = objectMapper.writeValueAsString(bookDto);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/books")
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatListBooksReturnsListOfBooks() throws Exception {
        AuthorEntity testAuthorEntityA = TestDataUtil.createTestAuthorEntityA();
        authorService.createAuthor(testAuthorEntityA);
        BookEntity testBookEntityA = TestDataUtil.createTestBookEntityA(testAuthorEntityA);
        bookService.createBook(testBookEntityA.getIsbn(), testBookEntityA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/books")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].isbn").value(testBookEntityA.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].title").value(testBookEntityA.getTitle())
        );
    }

    @Test
    public void testThatGetBookReturnsHttpStatus200OkWhenBookExists() throws Exception {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorEntityA();
        authorService.createAuthor(authorEntity);
        BookEntity bookEntity = TestDataUtil.createTestBookEntityA(authorEntity);
        bookService.createBook(bookEntity.getIsbn(),bookEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/"+bookEntity.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetBookReturnsHttpStatus404NotFoundWhenBookDoNotExists() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/ala-ma-kota")
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatGetBookSuccessfullyReturnsBookObject() throws Exception{
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorEntityA();
        authorService.createAuthor(authorEntity);
        BookEntity bookEntity = TestDataUtil.createTestBookEntityA(authorEntity);
        bookService.createBook(bookEntity.getIsbn(),bookEntity);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/"+bookEntity.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(bookEntity.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(bookEntity.getTitle())
        );

    }

    @Test
    public void testThatFullUpdateBookReturnsHttpStatus200OkWhenBookExists() throws Exception {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorEntityA();
        authorService.createAuthor(authorEntity);
        BookEntity bookEntity = TestDataUtil.createTestBookEntityA(authorEntity);
        bookService.createBook(bookEntity.getIsbn(),bookEntity);

        AuthorDto authorDto = TestDataUtil.createTestAuthorDtoA();
        BookDto bookDto = TestDataUtil.createTestBookDtoA(authorDto);

        String bookUpdateDtoJson = objectMapper.writeValueAsString(bookDto);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/books/"+bookEntity.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookUpdateDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }
}
