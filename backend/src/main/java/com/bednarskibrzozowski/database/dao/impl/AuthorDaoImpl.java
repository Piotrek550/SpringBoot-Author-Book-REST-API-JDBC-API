package com.bednarskibrzozowski.database.dao.impl;

import com.bednarskibrzozowski.database.dao.AuthorDao;
import com.bednarskibrzozowski.database.domain.entities.AuthorEntity;
import com.bednarskibrzozowski.database.domain.entities.BookEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
public class AuthorDaoImpl implements AuthorDao {

    private final JdbcTemplate jdbcTemplate;

    public AuthorRowMapper authorRowMapper;

    public AuthorDaoImpl(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        authorRowMapper = new AuthorRowMapper();
    }

    @Override
    public Optional<AuthorEntity> create(AuthorEntity author) {
          jdbcTemplate.update(
                  "INSERT INTO authors (name, age) VALUES (?, ?)",
                  author.getName(),
                  author.getAge()
          );
          return jdbcTemplate.query(
                "SELECT id, name, age FROM authors WHERE name = ? AND age = ? LIMIT 1",
                authorRowMapper,
                author.getName(),
                author.getAge()
        ).stream().findFirst();
    }

    @Override
    public Optional<AuthorEntity> findOne(long authorId) {
        List<AuthorEntity> results = jdbcTemplate.query(
                "SELECT id, name, age FROM authors WHERE id = ? LIMIT 1",
                authorRowMapper, authorId);
        return results.stream().findFirst();
    }

    @Override
    public List<AuthorEntity> find() {
        return jdbcTemplate.query(
                "SELECT id, name, age FROM authors",
                authorRowMapper
        );
    }

    @Override
    public Optional<AuthorEntity> update(long authorId, AuthorEntity author) {
        jdbcTemplate.update(
                "UPDATE authors SET name = ?, age = ? WHERE id = ?",
                author.getName(),
                author.getAge(),
                authorId
        );
        return jdbcTemplate.query(
                "SELECT id, name, age FROM authors WHERE id = ? LIMIT 1",
                authorRowMapper,
                authorId
        ).stream().findFirst();
    }

    @Override
    public Optional<AuthorEntity> delete(long authorId) {
        Optional<AuthorEntity> authorToDelete = this.findOne(authorId);
        jdbcTemplate.update(
                "DELETE FROM authors WHERE id = ?",
                authorId
        );
        return authorToDelete;
    }

    @Override
    public boolean isExists(long authorId) {
        return jdbcTemplate.query(
                "SELECT id, name, age FROM authors WHERE id = ? LIMIT 1",
                authorRowMapper,
                authorId
        ).stream().findFirst().isPresent();
    }

    public static class AuthorRowMapper implements RowMapper<AuthorEntity> {

        @Override
        public AuthorEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            return AuthorEntity.builder()
                    .id(rs.getLong("id"))
                    .name(rs.getString("name"))
                    .age(rs.getInt("age"))
                    .build();
        }
    }
}
