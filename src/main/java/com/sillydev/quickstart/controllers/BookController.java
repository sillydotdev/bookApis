package com.sillydev.quickstart.controllers;

import com.sillydev.quickstart.domain.dto.BookDto;
import com.sillydev.quickstart.domain.entities.BookEntity;
import com.sillydev.quickstart.mappers.Mapper;
import com.sillydev.quickstart.services.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class BookController {

    private BookService bookService;

    private Mapper<BookEntity, BookDto> bookMapper;

    public BookController(BookService bookService, Mapper<BookEntity, BookDto> bookMapper) {
        this.bookService = bookService;
        this.bookMapper = bookMapper;
    }

    @PutMapping(path = "/books/{isbn}")
    public ResponseEntity<BookDto> createUpdateBook(@PathVariable("isbn") String isbn, @RequestBody BookDto bookDto) {
        BookEntity bookEntity = bookMapper.mapFrom(bookDto);
        boolean bookExists = bookService.isExists(isbn);
        BookEntity savedBookEntity = bookService.createUpdateBook(isbn, bookEntity);
        BookDto savedUpdatedBookDto = bookMapper.mapTo(savedBookEntity);
        if(bookExists) {
            return new ResponseEntity<>(savedUpdatedBookDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(savedUpdatedBookDto, HttpStatus.CREATED);
        }
    }

    @GetMapping("/books")
    public Page<BookDto> getBooks(Pageable pageable) {
        Page<BookEntity> books = bookService.findAll(pageable);
//        return books.stream()
//                .map(bookMapper::mapTo)
//                .collect(Collectors.toList());
        return books.map(bookMapper::mapTo);
    }

    @GetMapping("/books/{isbn}")
    public ResponseEntity<BookDto> getBook(@PathVariable("isbn") String isbn) {
        Optional<BookEntity> book = bookService.getBook(isbn);
        return book.map(bookEntity -> {
            BookDto bookdto = bookMapper.mapTo(bookEntity);
            return new ResponseEntity<>(bookdto, HttpStatus.OK);
        }).orElse( new ResponseEntity<>(HttpStatus.NOT_FOUND) );
    }

    @PatchMapping("/books/{isbn}")
    public ResponseEntity<BookDto> patchBook(
            @PathVariable("isbn") String isbn,
            @RequestBody BookDto bookDto
    ) {
        if (!bookService.isExists(isbn)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        BookEntity bookEntity = bookMapper.mapFrom(bookDto);
        BookEntity updatedBookEntity = bookService.partialUpdate(isbn, bookEntity);
        return new ResponseEntity<>(bookMapper.mapTo(updatedBookEntity), HttpStatus.OK);

    }

    @DeleteMapping("/books/{isbn}")
    public ResponseEntity delete(@PathVariable("isbn") String isbn) {
        bookService.delete(isbn);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
