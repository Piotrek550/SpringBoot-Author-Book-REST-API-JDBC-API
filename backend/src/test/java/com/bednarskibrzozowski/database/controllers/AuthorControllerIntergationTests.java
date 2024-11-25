package com.bednarskibrzozowski.database.controllers;

import com.bednarskibrzozowski.database.TestDataUtil;
import com.bednarskibrzozowski.database.domain.dto.AuthorDto;
import com.bednarskibrzozowski.database.domain.entities.AuthorEntity;
import com.bednarskibrzozowski.database.services.AuthorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class AuthorControllerIntergationTests {

    private AuthorService authorService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Autowired
    public AuthorControllerIntergationTests(MockMvc mockMvc, AuthorService authorService) {
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
        this.authorService = authorService;
    }

    @Test
    public void testThatCreateAuthorSuccessfullyReturnsHttp201Created() throws Exception {
        AuthorEntity testAuthorA = TestDataUtil.createTestAuthorEntityA();
        testAuthorA.setId(null);
        String authorJson = objectMapper.writeValueAsString(testAuthorA);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateAuthorSuccessfullyReturnsSavedAuthor() throws Exception {
        AuthorEntity testAuthorA = TestDataUtil.createTestAuthorEntityA();
        testAuthorA.setId(null);
        String authorJson = objectMapper.writeValueAsString(testAuthorA);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("Zygmunt Krasicki")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(60)
        );
    }

    @Test
    public void testThatListAuthorsSuccessfullyReturnsHttp200Ok() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors")
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatListAuthorsSuccessfullyReturnsListOfAuthors() throws Exception {
        AuthorEntity testAuthorEntityA = TestDataUtil.createTestAuthorEntityA();
        testAuthorEntityA.setId(null);
        authorService.createAuthor(testAuthorEntityA);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].name").value("Zygmunt Krasicki")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].age").value(60)
        );
    }

    @Test
    public void testThatGetAuthorReturnsHttp200OkWhenAuthorExists() throws Exception {
        AuthorEntity testAuthorEntityA = TestDataUtil.createTestAuthorEntityA();
        authorService.createAuthor(testAuthorEntityA);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/"+testAuthorEntityA.getId())
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetAuthorReturnsHttp404NotFoundWhenAuthorDoNotExists() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/1000")
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatGetAuthorSuccessfullyReturnsAuthorObject() throws Exception {
        AuthorEntity testAuthorA = TestDataUtil.createTestAuthorEntityA();
        authorService.createAuthor(testAuthorA);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/"+testAuthorA.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(1L)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("Zygmunt Krasicki")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(60)
        );
    }
    
    @Test
    public void testThatFullUpdateAuthorReturnsHttp404NotFoundWhenAuthorDoNotExists() throws Exception {
        AuthorDto authorDto = TestDataUtil.createTestAuthorDtoA();
        String authorDtoJson = objectMapper.writeValueAsString(authorDto);
        mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/authors/1000")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatFullUpdateAuthorReturnsHttp200OkWhenAuthorExists() throws Exception {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorEntityA();
        Optional<AuthorEntity> savedAuthor = authorService.createAuthor(authorEntity);

        AuthorDto authorDto = TestDataUtil.createTestAuthorDtoA();
        String authorDtoJson = objectMapper.writeValueAsString(authorDto);
        mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/authors/"+savedAuthor.get().getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatFullUpdateUpdatesExistingAuthor() throws Exception {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorEntityA();
        Optional<AuthorEntity> savedAuthor = authorService.createAuthor(authorEntity);

        AuthorDto testAuthorDtoB = TestDataUtil.createTestAuthorDtoB();
        testAuthorDtoB.setId(savedAuthor.get().getId());

        String authorDtoUpdateJson = objectMapper.writeValueAsString(testAuthorDtoB);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/authors/"+savedAuthor.get().getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorDtoUpdateJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedAuthor.get().getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(testAuthorDtoB.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(testAuthorDtoB.getAge())
        );
    }
}
