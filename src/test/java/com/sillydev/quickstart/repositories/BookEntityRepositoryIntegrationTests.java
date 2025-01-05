package com.sillydev.quickstart.repositories;

import com.sillydev.quickstart.TestDataUtil;
import com.sillydev.quickstart.domain.entities.AuthorEntity;
import com.sillydev.quickstart.domain.entities.BookEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith( SpringExtension.class)
@DirtiesContext( classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookEntityRepositoryIntegrationTests {

    private BookRepository underTest;

    @Autowired
    public BookEntityRepositoryIntegrationTests(BookRepository underTest) {
        this.underTest = underTest;
    }

    @Test
    public void testThatBookCanBeCreatedAndRecalled() {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthor();
        BookEntity bookEntity = TestDataUtil.createTestBook(authorEntity);
        underTest.save(bookEntity);
        Optional<BookEntity> result = underTest.findById(bookEntity.getIsbn());
        assertThat(result.isPresent()).isTrue();
        authorEntity.setId(result.get().getAuthorEntity().getId());
        assertThat(result.get()).isEqualTo(bookEntity);
    }

    @Test
    public void testThatMultipleBooksCanBeCreatedAndRecalled() {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthor();
        BookEntity bookEntity = TestDataUtil.createTestBook(authorEntity);
        BookEntity bookEntity1 = TestDataUtil.createTestBook1(authorEntity);
        BookEntity bookEntity2 = TestDataUtil.createTestBook2(authorEntity);
        underTest.save(bookEntity);
        underTest.save(bookEntity1);
        underTest.save(bookEntity2);

        Iterable<BookEntity> result = underTest.findAll();
        assertThat(result).hasSize(3)
                .anySatisfy(b -> {
                    assertThat(b.getIsbn()).isEqualTo("1234567890");
                    assertThat(b.getTitle()).isEqualTo("The Hobbit");
                    assertThat(b.getAuthorEntity().getName()).isEqualTo("Kamran");
                    assertThat(b.getAuthorEntity().getAge()).isEqualTo(25);
                })
                .anySatisfy(b -> {
                    assertThat(b.getIsbn()).isEqualTo("9876543210");
                    assertThat(b.getTitle()).isEqualTo("Game of Thrones");
                    assertThat(b.getAuthorEntity().getName()).isEqualTo("Kamran");
                    assertThat(b.getAuthorEntity().getAge()).isEqualTo(25);
                })
                .anySatisfy(b -> {
                    assertThat(b.getIsbn()).isEqualTo("8556677898");
                    assertThat(b.getTitle()).isEqualTo("The Lord of The Rings");
                    assertThat(b.getAuthorEntity().getName()).isEqualTo("Kamran");
                    assertThat(b.getAuthorEntity().getAge()).isEqualTo(25);
                });

    }

    @Test
    public void testThatBookCanBeUpdated() {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthor();

        BookEntity bookEntity = TestDataUtil.createTestBook(authorEntity);
        underTest.save(bookEntity);

        bookEntity.setTitle("The Lords");
        underTest.save(bookEntity);

        Optional<BookEntity> result = underTest.findById(bookEntity.getIsbn());

        assert result.isPresent();
        BookEntity updatedBookEntity = result.get();
        assertThat(updatedBookEntity.getIsbn()).isEqualTo(bookEntity.getIsbn());
        assertThat(updatedBookEntity.getTitle()).isEqualTo("The Lords");
        assertThat(updatedBookEntity.getAuthorEntity().getName()).isEqualTo(bookEntity.getAuthorEntity().getName());
        assertThat(updatedBookEntity.getAuthorEntity().getAge()).isEqualTo(bookEntity.getAuthorEntity().getAge());
    }

    @Test
    public void testThatBookCanBeDeleted() {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthor();
        BookEntity bookEntity = TestDataUtil.createTestBook(authorEntity);
        underTest.save(bookEntity);

        underTest.deleteById(bookEntity.getIsbn());

        Optional<BookEntity> result = underTest.findById(bookEntity.getIsbn());
        assert !result.isPresent();
        assertThat(result.isPresent()).isFalse();
    }
}
