package com.sillydev.quickstart.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sillydev.quickstart.TestDataUtil;
import com.sillydev.quickstart.domain.entities.AuthorEntity;
import com.sillydev.quickstart.domain.entities.BookEntity;
import com.sillydev.quickstart.services.BookService;
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

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class BookControllerIntegrationTests {

    private MockMvc mockMvc;

    private BookService bookService;

    private ObjectMapper objectMapper;

    @Autowired
    public BookControllerIntegrationTests(MockMvc mockMvc, BookService bookService) {
        this.mockMvc = mockMvc;
        this.bookService = bookService;
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void testThatCreateUpdateBookSuccessfullyReturnsHttp201Created() throws Exception {
        BookEntity book = TestDataUtil.createTestBook(null);
        String bookJson = objectMapper.writeValueAsString(book);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/" + book.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateUpdateBookSuccessfullyReturnsHttp200OK() throws Exception {
        BookEntity book1 = TestDataUtil.createTestBook1(null);
        BookEntity savedBookEntity = bookService.createUpdateBook(book1.getIsbn(), book1);

        BookEntity book = TestDataUtil.createTestBook(null);
        book.setIsbn(savedBookEntity.getIsbn());
        String bookJson = objectMapper.writeValueAsString(book);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/" + book.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatUpdateBookSuccessfullyReturnsSavedUpdatedBook() throws Exception {
        BookEntity book = TestDataUtil.createTestBook(null);
        BookEntity savedBook = bookService.createUpdateBook(book.getIsbn(), book);

        BookEntity book1 = TestDataUtil.createTestBook1(null);
        book1.setIsbn(savedBook.getIsbn());
        String bookJson = objectMapper.writeValueAsString(book1);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/" + savedBook.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers
                        .jsonPath("$.isbn").value(savedBook.getIsbn())
        ).andExpect(
                MockMvcResultMatchers
                        .jsonPath("$.title").value(book1.getTitle())
        );
    }

    @Test
    public void testThatCreateBookSuccessfullyReturnsCreatedBook() throws Exception {
        BookEntity book = TestDataUtil.createTestBook(null);
        String bookJson = objectMapper.writeValueAsString(book);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/" + book.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers
                        .jsonPath("$.isbn").value(book.getIsbn())
        ).andExpect(
                MockMvcResultMatchers
                        .jsonPath("$.title").value(book.getTitle())
        );
    }

    @Test
    public void testThatGetBooksReturnsHttpStatus200() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/books")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetAuthorsReturnsListOfBooks() throws Exception {
        BookEntity book = TestDataUtil.createTestBook(null);
        bookService.createUpdateBook(book.getIsbn(), book);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/books")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].isbn").value(book.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].title").value(book.getTitle())
        );
    }

    @Test
    public void testThatGetBooksReturnHttpStatus200() throws Exception {
        BookEntity book = TestDataUtil.createTestBook(null);
        bookService.createUpdateBook(book.getIsbn(), book);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/" + book.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetBooksReturnHttpStatus404WhenNoBookExists() throws Exception {
        BookEntity book = TestDataUtil.createTestBook(null);
        bookService.createUpdateBook(book.getIsbn(), book);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/123456")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatPatchBookReturnsHttpStatus200() throws Exception {
        BookEntity book = TestDataUtil.createTestBook(null);
        bookService.createUpdateBook(book.getIsbn(), book);

        BookEntity book1 = TestDataUtil.createTestBook1(null);
        book1.setTitle("MKM");
        String bookJson = objectMapper.writeValueAsString(book1);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/books/"+book.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatPatchAuthorReturnsUpdatedAuthor() throws Exception {
        BookEntity book = TestDataUtil.createTestBook(null);
        bookService.createUpdateBook(book.getIsbn(), book);

        BookEntity book1 = TestDataUtil.createTestBook1(null);
        book1.setTitle("MKM");
        String bookJson = objectMapper.writeValueAsString(book1);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/books/" + book.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(book.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value("MKM")
        );
    }

    @Test
    public void testThatDeleteAuthorReturnsHttpStatus204ForNonExistingBook() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/books/999")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }

    @Test
    public void testThatDeleteAuthorReturnsHttpStatus204ForExistingAuthor() throws Exception {
        BookEntity book = TestDataUtil.createTestBook(null);
        BookEntity savedBook = bookService.createUpdateBook(book.getIsbn(), book);
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/authors/" + savedBook.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }

}
