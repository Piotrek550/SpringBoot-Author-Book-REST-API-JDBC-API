package com.devtiro.database.dao;

import com.devtiro.database.domain.entities.AuthorEntity;

import java.util.List;
import java.util.Optional;

public interface AuthorDao {

    void create(AuthorEntity author);

    Optional<AuthorEntity> findOne(long authorId);

    List<AuthorEntity> find();

    void update(long authorId, AuthorEntity author);

    void delete(long authorId);
}
