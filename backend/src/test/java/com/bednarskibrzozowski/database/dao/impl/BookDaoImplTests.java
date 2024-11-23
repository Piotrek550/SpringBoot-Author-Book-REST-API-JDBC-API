package com.bednarskibrzozowski.database.dao.impl;


import com.bednarskibrzozowski.database.TestDataUtil;
import com.bednarskibrzozowski.database.domain.entities.AuthorEntity;
import com.bednarskibrzozowski.database.domain.entities.BookEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BookDaoImplTests {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private BookDaoImpl underTest;

    @Test
    public void testThatCreateBookGeneratesCorrectSql() {
        AuthorEntity testAuthorA = TestDataUtil.createTestAuthorEntityA();
        BookEntity book = TestDataUtil.createTestBookEntityA(testAuthorA);

        underTest.create(book);

        verify(jdbcTemplate).update(
                eq("INSERT INTO books (isbn, title, author_id) VALUES (?, ?, ?)"),
                eq("978-1-2345-6789-0"),
                eq("Nie-boska komedia"),
                eq(1L)
        );
    }

    @Test
    public void testThatFindOneOneBookGeneratesCorrectSql() {
        underTest.findOne("978-1-2345-6789-0");

        verify(jdbcTemplate).query(
                eq("SELECT isbn, title, author_id FROM books WHERE isbn = ? LIMIT 1"),
                ArgumentMatchers.<BookDaoImpl.BookRowMapper>any(),
                eq("978-1-2345-6789-0")
        );
    }

    @Test
    public void testThatFindManyGeneratesCorrectSql() {
        underTest.find();

        verify(jdbcTemplate).query(
                eq("SELECT isbn, title, author_id FROM books"),
                ArgumentMatchers.<BookDaoImpl.BookRowMapper>any()
        );
    }

    @Test
    public void testThatUpdateBookGeneratesTheCorrectSql() {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorEntityA();
        BookEntity book = TestDataUtil.createTestBookEntityA(authorEntity);
        underTest.update(book.getIsbn(), book);

        verify(jdbcTemplate).update(
                eq("UPDATE books SET isbn = ?, title = ?, author_id = ? WHERE isbn = ?"),
                eq("978-1-2345-6789-0"),
                eq("Nie-boska komedia"),
                eq(1L),
                eq("978-1-2345-6789-0")
        );
    }

    @Test
    public void testThatDeleteBookGeneratesTheCorrectSql() {
        underTest.delete("978-1-2345-6789-0");
        verify(jdbcTemplate).update(
                eq("DELETE FROM books WHERE isbn = ?"),
                eq("978-1-2345-6789-0")
        );
    }

}
