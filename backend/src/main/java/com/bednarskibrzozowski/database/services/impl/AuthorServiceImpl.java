package com.bednarskibrzozowski.database.services.impl;

import com.bednarskibrzozowski.database.dao.AuthorDao;
import com.bednarskibrzozowski.database.domain.entities.AuthorEntity;
import com.bednarskibrzozowski.database.services.AuthorService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class AuthorServiceImpl implements AuthorService {

    private AuthorDao authorDao;

    public AuthorServiceImpl(AuthorDao authorDao) {
        this.authorDao = authorDao;
    }

    @Override
    public Optional<AuthorEntity> createAuthor(AuthorEntity authorEntity) {
        return authorDao.create(authorEntity);
    }

    @Override
    public List<AuthorEntity> findAll() {
        return StreamSupport.stream(authorDao
                .find()
                .spliterator(),
                false)
                .toList();
    }

    @Override
    public Optional<AuthorEntity> findOne(Long authorId) {
        return authorDao.findOne(authorId);
    }

    public Optional<AuthorEntity> update(Long authorId, AuthorEntity authorEntity)
    {
        return authorDao.update(authorId, authorEntity);
    }

    @Override
    public Optional<AuthorEntity> delete(Long authorId) {
        return authorDao.delete(authorId);
    }

    @Override
    public boolean isExists(Long authorId) {
        return authorDao.isExists(authorId);
    }

    @Override
    public Optional<AuthorEntity> partialUpdate(Long authorId, AuthorEntity authorEntity) {
        return authorDao.findOne(authorId).map(existingAuthor -> {
            Optional.ofNullable(authorEntity.getName()).ifPresent(existingAuthor::setName);
            Optional.ofNullable(authorEntity.getAge()).ifPresent(existingAuthor::setAge);
            return authorDao.update(authorId,existingAuthor);
        }).orElseThrow(
                () -> new RuntimeException("Autor nie istnieje!")
        );
    }
}
