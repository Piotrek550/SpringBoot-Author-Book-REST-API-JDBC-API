package com.bednarskibrzozowski.database.dao;

import com.bednarskibrzozowski.database.domain.entities.AuthorEntity;

import java.util.List;
import java.util.Optional;

public interface AuthorDao {

    Optional<AuthorEntity> create(AuthorEntity author);

    Optional<AuthorEntity> findOne(long authorId);

    List<AuthorEntity> find();

    Optional<AuthorEntity> update(long authorId, AuthorEntity author);

    Optional<AuthorEntity> delete(long authorId);

    boolean isExists(long authorId);
}
