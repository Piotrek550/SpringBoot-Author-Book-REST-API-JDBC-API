package com.bednarskibrzozowski.database.mappers.impl;

import com.bednarskibrzozowski.database.dao.AuthorDao;
import com.bednarskibrzozowski.database.domain.entities.BookEntity;
import com.bednarskibrzozowski.database.domain.dto.AuthorDto;
import com.bednarskibrzozowski.database.domain.dto.BookDto;
import com.bednarskibrzozowski.database.domain.entities.AuthorEntity;
import com.bednarskibrzozowski.database.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;


import java.util.Optional;

@Component
public class BookMapperImpl implements Mapper<BookEntity, BookDto> {

    private ModelMapper modelMapper;

    private AuthorDao authorDao;

    public BookMapperImpl(ModelMapper modelMapper, AuthorDao authorDao) {
        this.modelMapper = modelMapper;
        this.authorDao = authorDao;
    }

    @Override
    public BookDto mapTo(BookEntity book) {
        Optional<AuthorEntity> joinAuthor = authorDao.findOne(book.getAuthorId());
        if(joinAuthor.isPresent()) {
            AuthorDto joinAuthorDto = modelMapper.map(joinAuthor.get(),AuthorDto.class);
            return BookDto.builder()
                    .isbn(book.getIsbn())
                    .title(book.getTitle())
                    .author(joinAuthorDto)
                    .build();
        }
        else {
            return BookDto.builder()
                    .isbn(book.getIsbn())
                    .title(book.getTitle())
                    .author(null)
                    .build();
        }
    }

    @Override
    public BookEntity mapFrom(BookDto bookDto) {
        return BookEntity.builder()
                .isbn(bookDto.getIsbn())
                .title(bookDto.getTitle())
                .authorId(bookDto.getAuthor().getId())
                .build();
    }
}
