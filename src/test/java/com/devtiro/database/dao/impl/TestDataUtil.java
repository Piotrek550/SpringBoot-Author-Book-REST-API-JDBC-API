package com.devtiro.database.dao.impl;

import com.devtiro.database.domain.Author;
import com.devtiro.database.domain.Book;

public final class TestDataUtil {
    private TestDataUtil(){}

    public static Author createTestAuthorA() {
        return Author.builder()
                .id(1L)
                .name("Zygmunt Krasicki")
                .age(60)
                .build();
    }

    public static Author createTestAuthorB() {
        return Author.builder()
                .id(2L)
                .name("Stanislaw Wyspianski")
                .age(55)
                .build();
    }

    public static Author createTestAuthorC() {
        return Author.builder()
                .id(3L)
                .name("SBoleslaw Prus")
                .age(47)
                .build();
    }

    public static Book createTestBook() {
        return Book.builder()
                .isbn("978-1-2345-6789-0")
                .title("Nie-boska komedia")
                .authorId(1L)
                .build();
    }
}
