package com.example.bookstore.controller;

import com.example.bookstore.dto.BookDto;
import com.example.bookstore.repository.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

@WebMvcTest(BookRestController.class)
class BookRestControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookRepository bookRepository;

    @Test
    void createBookTest() throws Exception {
        Mockito.doNothing().when(bookRepository).addBook(ArgumentMatchers.any());
        String jsonString = objectMapper.readTree(
                BookRestControllerWebMvcTest.class.getResourceAsStream("/controller/createBookTestRequest.json")
        ).toString();

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/create-book")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonString)
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(jsonString));

        Mockito.verify(bookRepository).addBook(ArgumentMatchers.any());
    }

    @Test
    void getBooksWithoutFiltersTest() throws Exception {
        String jsonString = objectMapper.readTree(
                BookRestControllerWebMvcTest.class
                        .getResourceAsStream("/controller/getBooksWithoutFiltersResponse.json")
        ).toString();
        List<BookDto> bookList = Arrays.asList(objectMapper.readValue(jsonString, BookDto[].class));
        Mockito.doReturn(bookList).when(bookRepository).getAllBooks();

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/get-books")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(jsonString));

        Mockito.verify(bookRepository).getAllBooks();
    }

    @Test
    void getBooksWithFiltersTest() throws Exception {
        String jsonStringAll = objectMapper.readTree(
                BookRestControllerWebMvcTest.class
                        .getResourceAsStream("/controller/getBooksWithFiltersAll.json")
        ).toString();
        List<BookDto> bookList = Arrays.asList(objectMapper.readValue(jsonStringAll, BookDto[].class));
        Mockito.doReturn(bookList).when(bookRepository).getAllBooks();

        String jsonStringResponse = objectMapper.readTree(
                BookRestControllerWebMvcTest.class
                        .getResourceAsStream("/controller/getBooksWithFiltersResponse.json")
        ).toString();

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/get-books")
                                .queryParam("name", "a")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(jsonStringResponse));

        Mockito.verify(bookRepository).getAllBooks();
    }

}