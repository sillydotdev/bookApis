package com.sillydev.quickstart;

import com.sillydev.quickstart.domain.dto.AuthorDto;
import com.sillydev.quickstart.domain.entities.AuthorEntity;
import com.sillydev.quickstart.domain.entities.BookEntity;

public final class TestDataUtil {
    private TestDataUtil() {}

    public static AuthorEntity createTestAuthor() {
        AuthorEntity authorEntity = AuthorEntity.builder()
                .name("Kamran")
                .age(25)
                .build();
        return authorEntity;
    }

    public static AuthorEntity createTestAuthor1() {
        AuthorEntity authorEntity = AuthorEntity.builder()
                .name("Danish")
                .age(24)
                .build();
        return authorEntity;
    }

    public static AuthorEntity createTestAuthor2() {
        AuthorEntity authorEntity = AuthorEntity.builder()
                .name("Aamir")
                .age(29)
                .build();
        return authorEntity;
    }

    public static BookEntity createTestBook(final AuthorEntity authorEntity) {
        return BookEntity.builder()
                .isbn("1234567890")
                .title("The Hobbit")
                .authorEntity(authorEntity)
                .build();
    }

    public static BookEntity createTestBook1(final AuthorEntity authorEntity) {
        return BookEntity.builder()
                .isbn("9876543210")
                .title("Game of Thrones")
                .authorEntity(authorEntity)
                .build();
    }

    public static BookEntity createTestBook2(final AuthorEntity authorEntity) {
        return BookEntity.builder()
                .isbn("8556677898")
                .title("The Lord of The Rings")
                .authorEntity(authorEntity)
                .build();
    }
}
