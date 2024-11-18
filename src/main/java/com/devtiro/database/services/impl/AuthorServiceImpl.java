package com.devtiro.database.services.impl;

import com.devtiro.database.dao.AuthorDao;
import com.devtiro.database.domain.entities.AuthorEntity;
import com.devtiro.database.services.AuthorService;
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
    public AuthorEntity createAuthor(AuthorEntity authorEntity) {
        authorDao.create(authorEntity);
        return authorEntity;
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
}
