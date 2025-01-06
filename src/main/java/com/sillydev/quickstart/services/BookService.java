package com.sillydev.quickstart.services;

import com.sillydev.quickstart.domain.entities.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BookService {

    BookEntity createUpdateBook(String isbn, BookEntity bookEntity);

    List<BookEntity> getAllBooks();

    Page<BookEntity> findAll(Pageable pageable);

    Optional<BookEntity> getBook(String isbn);

    boolean isExists(String isbn);

    BookEntity partialUpdate(String isbn, BookEntity bookEntity);

    void delete(String isbn);
}
