package com.bednarskibrzozowski.database.dao.impl;

import com.bednarskibrzozowski.database.dao.BookDao;
import com.bednarskibrzozowski.database.domain.entities.BookEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
public class BookDaoImpl implements BookDao {

    private final JdbcTemplate jdbcTemplate;

    public BookRowMapper bookRowMapper;

    public BookDaoImpl(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.bookRowMapper = new BookRowMapper();
    }

    @Override
    public Optional<BookEntity> create(BookEntity book) {
        jdbcTemplate.update(
                "INSERT INTO books (isbn, title, author_id) VALUES (?, ?, ?)",
                book.getIsbn(),
                book.getTitle(),
                book.getAuthorId()
        );

        return this.findOne(book.getIsbn());
    }

    @Override
    public Optional<BookEntity> findOne(String isbn) {
        List<BookEntity> result = jdbcTemplate.query(
                "SELECT isbn, title, author_id FROM books WHERE isbn = ? LIMIT 1",
                bookRowMapper,
                isbn
        );
        return result.stream().findFirst();
    }

    @Override
    public List<BookEntity> find() {
        return jdbcTemplate.query(
                "SELECT isbn, title, author_id FROM books",
                bookRowMapper);
    }

    @Override
    public Optional<BookEntity> update(String isbn, BookEntity book) {
        jdbcTemplate.update(
                "UPDATE books SET isbn = ?, title = ?, author_id = ? WHERE isbn = ?",
                book.getIsbn(),
                book.getTitle(),
                book.getAuthorId(),
                isbn
        );
        return this.findOne(isbn);
    }

    @Override
    public Optional<BookEntity> delete(String isbn) {
        Optional<BookEntity> bookToDelete = this.findOne(isbn);
        jdbcTemplate.update("DELETE FROM books WHERE isbn = ?",isbn);
        return bookToDelete;
    }

    @Override
    public boolean isExists(String isbn) {
        return this.findOne(isbn).isPresent();
    }

    public static class BookRowMapper implements RowMapper<BookEntity> {

        @Override
        public BookEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            return BookEntity.builder()
                    .isbn(rs.getString("isbn"))
                    .title(rs.getString("title"))
                    .authorId(rs.getLong("author_id"))
                    .build();
        }
    }
}
