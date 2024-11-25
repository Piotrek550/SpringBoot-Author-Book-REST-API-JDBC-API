package com.bednarskibrzozowski.database;

import com.bednarskibrzozowski.database.domain.dto.AuthorDto;
import com.bednarskibrzozowski.database.domain.dto.BookDto;
import com.bednarskibrzozowski.database.domain.entities.AuthorEntity;
import com.bednarskibrzozowski.database.domain.entities.BookEntity;

public final class TestDataUtil {
    private TestDataUtil(){}

    public static AuthorEntity createTestAuthorEntityA() {
        return AuthorEntity.builder()
                .id(1L)
                .name("Zygmunt Krasicki")
                .age(60)
                .build();
    }

    public static AuthorEntity createTestAuthorEntityB() {
        return AuthorEntity.builder()
                .id(2L)
                .name("Stanislaw Wyspianski")
                .age(55)
                .build();
    }

    public static AuthorEntity createTestAuthorEntityC() {
        return AuthorEntity.builder()
                .id(3L)
                .name("Boleslaw Prus")
                .age(47)
                .build();
    }

    public static BookEntity createTestBookEntityA(final AuthorEntity authorEntity) {
        return BookEntity.builder()
                .isbn("978-1-2345-6789-0")
                .title("Nie-boska komedia")
                .authorId(authorEntity.getId())
                .build();
    }

    public static BookEntity createTestBookEntityB(final AuthorEntity authorEntity) {
        return BookEntity.builder()
                .isbn("123-4-5678-3764-0")
                .title("Wesele")
                .authorId(authorEntity.getId())
                .build();
    }

    public static BookEntity createTestBookEntityC(final AuthorEntity authorEntity) {
        return BookEntity.builder()
                .isbn("734-2-4551-9934-0")
                .title("Lalka")
                .authorId(authorEntity.getId())
                .build();
    }

    public static BookDto createTestBookDtoA(final AuthorDto authorDto) {
        return BookDto.builder()
                .isbn("978-1-2345-6789-0")
                .title("Nie-boska komedia")
                .author(authorDto)
                .build();
    }

    public static BookDto createTestBookDtoB(final AuthorDto authorDto) {
        return BookDto.builder()
                .isbn("123-4-5678-3764-0")
                .title("Wesele")
                .author(authorDto)
                .build();
    }

    public static BookDto createTestBookDtoC(final AuthorDto authorDto) {
        return BookDto.builder()
                .isbn("734-2-4551-9934-0")
                .title("Lalka")
                .author(authorDto)
                .build();
    }

    public static AuthorDto createTestAuthorDtoA() {
        return AuthorDto.builder()
                .id(1L)
                .name("Zygmunt Krasicki")
                .age(60)
                .build();
    }

    public static AuthorDto createTestAuthorDtoB() {
        return AuthorDto.builder()
                .id(2L)
                .name("Stanislaw Wyspianski")
                .age(55)
                .build();
    }

    public static AuthorDto createTestAuthorDtoC() {
        return AuthorDto.builder()
                .id(3L)
                .name("Boleslaw Prus")
                .age(47)
                .build();
    }
}
