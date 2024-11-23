package com.bednarskibrzozowski.database.dao.impl;

import com.bednarskibrzozowski.database.TestDataUtil;
import com.bednarskibrzozowski.database.dao.AuthorDao;
import com.bednarskibrzozowski.database.domain.entities.AuthorEntity;
import com.bednarskibrzozowski.database.domain.entities.BookEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookDaoImplIntegrationTests {

    private AuthorDao authorDao;

    private BookDaoImpl underTest;

    @Autowired
    public BookDaoImplIntegrationTests(BookDaoImpl underTest, AuthorDao authorDao) {
        this.underTest = underTest;
        this.authorDao = authorDao;
    }

    @Test
    public void testThatBookCanBeCreatedAndRecalled() {
        AuthorEntity author = TestDataUtil.createTestAuthorEntityA();
        authorDao.create(author);
        BookEntity book = TestDataUtil.createTestBookEntityA(author);
        book.setAuthorId(author.getId());
        underTest.create(book);
        Optional<BookEntity> result = underTest.findOne(book.getIsbn());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(book);
    }

    @Test
    public void testThatBookCanBeUpdated() {
        AuthorEntity author = TestDataUtil.createTestAuthorEntityA();
        authorDao.create(author);
        BookEntity book = TestDataUtil.createTestBookEntityA(author);
        underTest.create(book);
        book.setTitle("TITLE UPDATED");
        underTest.update(book.getIsbn(), book);
        Optional<BookEntity> result = underTest.findOne(book.getIsbn());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(book);
    }

    @Test
    public void testThatBookCanBeDeleted() {
        AuthorEntity author = TestDataUtil.createTestAuthorEntityA();
        authorDao.create(author);
        BookEntity book = TestDataUtil.createTestBookEntityB(author);
        underTest.create(book);
        underTest.delete(book.getIsbn());
        Optional<BookEntity> result = underTest.findOne(book.getIsbn());
        assertThat(result).isEmpty();
    }
}
