package com.bednarskibrzozowski.database.dao.impl;

import com.bednarskibrzozowski.database.TestDataUtil;
import com.bednarskibrzozowski.database.domain.entities.AuthorEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AuthorDaoImplIntergationTests {

    private AuthorDaoImpl underTest;

    @Autowired
    public AuthorDaoImplIntergationTests(AuthorDaoImpl underTest) {
        this.underTest = underTest;
    }

    @Test
    public void testThatAuthorCanBeCreatedAndRecalled() {
        AuthorEntity author = TestDataUtil.createTestAuthorEntityA();
        underTest.create(author);
        Optional<AuthorEntity> result = underTest.findOne(author.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(author);
    }

    @Test
    public void testThatMultipleAuthorsCanBeCreatedAndRecalled() {
        AuthorEntity authorA = TestDataUtil.createTestAuthorEntityA();
        AuthorEntity authorB = TestDataUtil.createTestAuthorEntityB();
        AuthorEntity authorC = TestDataUtil.createTestAuthorEntityC();
        underTest.create(authorA);
        underTest.create(authorB);
        underTest.create(authorC);

        List<AuthorEntity> result = underTest.find();
        assertThat(result).hasSize(3);
        assertThat(result).containsExactly(authorA, authorB, authorC);
    }

    @Test
    public void testThatAuthorCanBeUpdated() {
        AuthorEntity authorA = TestDataUtil.createTestAuthorEntityA();
        underTest.create(authorA);
        authorA.setName("ZMIENIONE");
        underTest.update(authorA.getId(),authorA);
        Optional<AuthorEntity> result = underTest.findOne(authorA.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(authorA);
    }

    @Test
    public void testThatAuthorCanBeDeleted() {
        AuthorEntity authorA = TestDataUtil.createTestAuthorEntityA();
        underTest.create(authorA);
        underTest.delete(authorA.getId());
        Optional<AuthorEntity> result = underTest.findOne(authorA.getId());
        assertThat(result).isEmpty();
    }
}
