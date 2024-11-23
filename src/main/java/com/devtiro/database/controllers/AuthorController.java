package com.devtiro.database.controllers;

import com.devtiro.database.domain.entities.AuthorEntity;
import com.devtiro.database.domain.dto.AuthorDto;
import com.devtiro.database.mappers.Mapper;
import com.devtiro.database.services.AuthorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class AuthorController {

    private AuthorService authorService;

    private Mapper<AuthorEntity, AuthorDto> authorMapper;

    public AuthorController(AuthorService authorService, Mapper<AuthorEntity, AuthorDto> authorMapper) {
        this.authorService = authorService;
        this.authorMapper = authorMapper;
    }

    @PostMapping(path = "/authors")
    public ResponseEntity<AuthorDto> createAuthor(@RequestBody AuthorDto author) {
        AuthorEntity authorEntity = authorMapper.mapFrom(author);
        Optional<AuthorEntity> savedAuthorEntity = authorService.createAuthor(authorEntity);
        return savedAuthorEntity.map(entity ->
                        new ResponseEntity<>(authorMapper.mapTo(entity), HttpStatus.CREATED)
                ).orElse(
                        new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE)
        );

    }

    @GetMapping(path = "/authors")
    public List<AuthorDto> listAuthors() {
        List<AuthorEntity> authors = authorService.findAll();
        return authors.stream().map(authorMapper::mapTo).toList();
    }

    @GetMapping(path = "/authors/{id}")
    public ResponseEntity<AuthorDto> getAuthor(@PathVariable("id") Long id) {
        Optional<AuthorEntity> foundAuthor = authorService.findOne(id);
        return foundAuthor.map(authorEntity -> {
            AuthorDto authorDto = authorMapper.mapTo(authorEntity);
            return new ResponseEntity<>(authorDto, HttpStatus.OK);
        }).orElse(
                new ResponseEntity<>(HttpStatus.NOT_FOUND)
        );
    }

    @PutMapping(path = "/authors/{id}")
    public ResponseEntity<AuthorDto> updateAuthor(
            @PathVariable("id") Long id,
            @RequestBody AuthorDto authorDto) {

        if(!authorService.isExists(id))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Optional<AuthorEntity> updatedAuthor = authorService.update(id, authorMapper.mapFrom(authorDto));
        return updatedAuthor.map(authorEntity -> {
            AuthorDto updatedAuthorDto = authorMapper.mapTo(updatedAuthor.get());
            return new ResponseEntity<>(updatedAuthorDto, HttpStatus.OK);
        }).orElse(
                new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE)
        );
    }

    @PatchMapping(path = "/authors/{id}")
    public ResponseEntity<AuthorDto> partialUpdateAuthor(
            @PathVariable("id") Long id,
            @RequestBody AuthorDto authorDto
    )
    {
        if(!authorService.isExists(id))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        AuthorEntity authorEntity = authorMapper.mapFrom(authorDto);
        Optional<AuthorEntity> partialUpdatedAuthor = authorService.partialUpdate(id, authorEntity);
        return partialUpdatedAuthor.map(updatedEntity -> {
            AuthorDto partialUpdatedAuthorDto = authorMapper.mapTo(updatedEntity);
            return new ResponseEntity<>(partialUpdatedAuthorDto, HttpStatus.OK);
        }).orElse(
                new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE)
        );
    }

    @DeleteMapping(path = "/authors/{id}")
    public ResponseEntity<AuthorDto> deleteAuthor(@PathVariable("id") Long id) {

        Optional<AuthorEntity> deletedAuthor = authorService.delete(id);
        return deletedAuthor.map(authorEntity -> {
            AuthorDto deletedAuthorDto = authorMapper.mapTo(deletedAuthor.get());
            return new ResponseEntity<>(deletedAuthorDto, HttpStatus.OK);
        }).orElse(
                new ResponseEntity<>(HttpStatus.NOT_FOUND)
        );
    }

}
