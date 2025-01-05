package com.sillydev.quickstart.repositories;

import com.sillydev.quickstart.TestDataUtil;
import com.sillydev.quickstart.domain.entities.AuthorEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AuthorEntityRepositoryIntegrationTests {

    private AuthorRepository underTest;

    @Autowired
    public AuthorEntityRepositoryIntegrationTests(AuthorRepository underTest) {
        this.underTest = underTest;
    }

    @Test
    public void testThatAuthorCanBeCreatedAndRecalled() {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthor();
        underTest.save(authorEntity);
        Optional<AuthorEntity> result = underTest.findById(authorEntity.getId());

        assert result.isPresent();
        assertThat(result.get()).isEqualTo(authorEntity);
    }

    @Test
    public void testThatMultipleAuthorsCanBeCreatedAndRecalled() {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthor();
        AuthorEntity authorEntity1 = TestDataUtil.createTestAuthor1();
        AuthorEntity authorEntity2 = TestDataUtil.createTestAuthor2();
        underTest.save(authorEntity);
        underTest.save(authorEntity1);
        underTest.save(authorEntity2);

        Iterable<AuthorEntity> result = underTest.findAll();

        assertThat(result).hasSize(3)
                .contains(authorEntity, authorEntity1, authorEntity2);
    }

    @Test
    public void testThatAuthorCanBeUpdated() {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthor();
        underTest.save(authorEntity);
        authorEntity.setName("Rabiya");
        underTest.save(authorEntity);
        Optional<AuthorEntity> result = underTest.findById(authorEntity.getId());
        assert result.isPresent();
        assertThat(result.get()).isEqualTo(authorEntity);
    }

    @Test
    public void testThatAuthorCanBeDeleted() {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthor();
        underTest.save(authorEntity);
        underTest.deleteById(authorEntity.getId());
        Optional<AuthorEntity> result = underTest.findById(authorEntity.getId());
        assert !result.isPresent();
        assertThat(result.isPresent()).isFalse();
    }

    @Test
    public void testThatGetAuthorWithAgeLessThan() {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthor();
        AuthorEntity authorEntity1 = TestDataUtil.createTestAuthor1();
        AuthorEntity authorEntity2 = TestDataUtil.createTestAuthor2();
        underTest.save(authorEntity);
        underTest.save(authorEntity1);
        underTest.save(authorEntity2);

        Iterable<AuthorEntity> result = underTest.ageLessThan(26);

        assertThat(result).containsExactly(authorEntity, authorEntity1);
    }

    @Test
    public void testThatGetAuthorWithAgeGreaterThan() {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthor();
        AuthorEntity authorEntity1 = TestDataUtil.createTestAuthor1();
        AuthorEntity authorEntity2 = TestDataUtil.createTestAuthor2();
        underTest.save(authorEntity);
        underTest.save(authorEntity1);
        underTest.save(authorEntity2);

        Iterable<AuthorEntity> result = underTest.findAuthorsWithAgerGreaterThan(26);

        assertThat(result).containsExactly(authorEntity2);
    }
}
