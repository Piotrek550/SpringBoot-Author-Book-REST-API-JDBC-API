package com.bednarskibrzozowski.database.services;

import com.bednarskibrzozowski.database.domain.entities.AuthorEntity;

import java.util.List;
import java.util.Optional;

public interface AuthorService {

    Optional<AuthorEntity> createAuthor(AuthorEntity authorEntity);

    List<AuthorEntity> findAll();

    Optional<AuthorEntity> findOne(Long authorId);

    Optional<AuthorEntity> update(Long authorId, AuthorEntity authorEntity);

    Optional<AuthorEntity> delete(Long authorId);

    boolean isExists(Long authorId);
    Optional<AuthorEntity> partialUpdate(Long authorId, AuthorEntity authorEntity);
}
