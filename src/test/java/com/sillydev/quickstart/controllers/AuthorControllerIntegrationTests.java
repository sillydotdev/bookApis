package com.sillydev.quickstart.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sillydev.quickstart.TestDataUtil;
import com.sillydev.quickstart.domain.dto.AuthorDto;
import com.sillydev.quickstart.domain.entities.AuthorEntity;
import com.sillydev.quickstart.services.AuthorService;
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
public class AuthorControllerIntegrationTests {

    private MockMvc mockMvc;

    private AuthorService authorService;

    private ObjectMapper objectMapper;

    @Autowired
    public AuthorControllerIntegrationTests(MockMvc mockMvc, AuthorService authorService) {
        this.mockMvc = mockMvc;
        this.authorService = authorService;
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void testThatCreateAuthorSuccessfullyReturnsHttp201Created() throws Exception {
        AuthorEntity author = TestDataUtil.createTestAuthor();
        author.setId(null);
        String authorJson = objectMapper.writeValueAsString(author);

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
        AuthorEntity author = TestDataUtil.createTestAuthor();
        author.setId(null);
        String authorJson = objectMapper.writeValueAsString(author);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers
                        .jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers
                        .jsonPath("$.name").value("Kamran")
        ).andExpect(
                MockMvcResultMatchers
                        .jsonPath("$.age").value(25)
        );
    }

    @Test
    public void testThatGetAuthorsReturnsHttpStatus200() throws Exception {

        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetAuthorsReturnsListOfAuthors() throws Exception {

        AuthorEntity author = TestDataUtil.createTestAuthor();
        authorService.save(author);

        AuthorEntity author1 = TestDataUtil.createTestAuthor1();
        authorService.save(author1);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].id").value(author.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].name").value(author.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].age").value(author.getAge())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[1].id").value(author1.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[1].name").value(author1.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[1].age").value(author1.getAge())
        );
    }

    @Test
    public void testThatGetAuthorReturnsHttpStatus200WhenAuthorExists() throws Exception {
        AuthorEntity author = TestDataUtil.createTestAuthor();
        authorService.save(author);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetAuthorReturnsHttpStatus404WhenNoAuthorExists() throws Exception {
        AuthorEntity author = TestDataUtil.createTestAuthor();
        authorService.save(author);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/11")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatGetAuthorReturnsAuthor() throws Exception {
        AuthorEntity author = TestDataUtil.createTestAuthor();
        authorService.save(author);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(1)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("Kamran")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(25)
        );
    }

    @Test
    public void testThatUpdateAuthorReturnsHttpStatus404WhenNoAuthorExists() throws Exception {
        AuthorEntity author = TestDataUtil.createTestAuthor();
        String authorJson = objectMapper.writeValueAsString(author);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/authors/11")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatUpdateAuthorReturnsHttpStatus200WhenAuthorExists() throws Exception {
        AuthorEntity author = TestDataUtil.createTestAuthor();
        AuthorEntity savedAuthor = authorService.save(author);

        AuthorEntity author1 = TestDataUtil.createTestAuthor1();
        String authorJson = objectMapper.writeValueAsString(author1);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/authors/"+savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatUpdateAuthorReturnsUpdatedAuthor() throws Exception {
        AuthorEntity author = TestDataUtil.createTestAuthor();
        AuthorEntity savedAuthor = authorService.save(author);

        AuthorEntity author1 = TestDataUtil.createTestAuthor1();
        author1.setId(savedAuthor.getId());

        String authorJson = objectMapper.writeValueAsString(author1);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/authors/"+savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedAuthor.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(author1.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(author1.getAge())
        );
    }

    @Test
    public void testThatPatchAuthorReturnsHttpStatus200() throws Exception {
        AuthorEntity author = TestDataUtil.createTestAuthor();
        AuthorEntity savedAuthor = authorService.save(author);

        AuthorEntity author1 = TestDataUtil.createTestAuthor1();
        author1.setName("MKM");
        String authorJson = objectMapper.writeValueAsString(author1);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/authors/"+savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatPatchAuthorReturnsUpdatedAuthor() throws Exception {
        AuthorEntity author = TestDataUtil.createTestAuthor();
        AuthorEntity savedAuthor = authorService.save(author);

        AuthorEntity author1 = TestDataUtil.createTestAuthor1();
        author1.setName("MKM");
        String authorJson = objectMapper.writeValueAsString(author1);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/authors/" + savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedAuthor.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("MKM")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(author1.getAge())
        );
    }

    @Test
    public void testThatDeleteAuthorReturnsHttpStatus204ForNonExistingAuthor() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/authors/999")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }

    @Test
    public void testThatDeleteAuthorReturnsHttpStatus204ForExistingAuthor() throws Exception {
        AuthorEntity author = TestDataUtil.createTestAuthor();
        AuthorEntity savedAuthor = authorService.save(author);
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/authors/" + savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }
}
