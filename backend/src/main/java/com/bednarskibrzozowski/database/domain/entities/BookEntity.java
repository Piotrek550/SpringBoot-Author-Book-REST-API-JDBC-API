package com.bednarskibrzozowski.database.domain.entities;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookEntity {

    private String isbn;

    private String title;

    private Long authorId;
}
