package com.example.bookstore.controller;

import com.example.bookstore.dto.BookDto;
import com.example.bookstore.repository.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.assertj.core.api.Assertions;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookRestControllerIntegrationTest {

    @SpyBean
    private BookRepository bookRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @LocalServerPort
    void setPort(int port) {
        RestAssured.port = port;
    }

    @AfterEach
    void clearRepository() {
        bookRepository.clearRepository();
    }

    @Test
    void createBookTest() throws IOException {
        String jsonString = objectMapper.readTree(
                BookRestControllerIntegrationTest.class.getResourceAsStream("/controller/createBookTestRequest.json")
        ).toString();
        BookDto bookDto = objectMapper.readValue(jsonString, BookDto.class);

        RestAssured
                .given()
                    .contentType(ContentType.JSON)
                    .body(jsonString)
                .when()
                    .post("/create-book")
                .then()
                    .statusCode(201)
                    .body("title", CoreMatchers.is(bookDto.title()))
                    .body("isbn", CoreMatchers.is(bookDto.isbn()))
                    .body("author", CoreMatchers.is(bookDto.author()));

        Mockito.verify(bookRepository).addBook(ArgumentMatchers.any());
        Assertions.assertThat(bookRepository.getAllBooks()).contains(bookDto);
    }

    @Test
    void getBooksWithoutFiltersTest() throws IOException {
        String jsonString = objectMapper.readTree(
                BookRestControllerWebMvcTest.class
                        .getResourceAsStream("/controller/getBooksWithoutFiltersResponse.json")
        ).toString();
        List<BookDto> bookList = Arrays.asList(objectMapper.readValue(jsonString, BookDto[].class));
        bookList.forEach(bookDto -> bookRepository.addBook(bookDto));

        String response = RestAssured
                .when()
                    .get("/get-books")
                .then()
                    .statusCode(200)
                    .extract()
                    .body()
                    .asString();

        Mockito.verify(bookRepository).getAllBooks();

        List<BookDto> responseList = Arrays.asList(objectMapper.readValue(response, BookDto[].class));
        Assertions.assertThat(responseList)
                .containsExactlyInAnyOrderElementsOf(bookList);
    }

    @Test
    void getBooksWithFiltersTest() throws IOException {
        String jsonString = objectMapper.readTree(
                BookRestControllerWebMvcTest.class
                        .getResourceAsStream("/controller/getBooksWithoutFiltersResponse.json")
        ).toString();
        List<BookDto> bookList = Arrays.asList(objectMapper.readValue(jsonString, BookDto[].class));
        bookList.forEach(bookDto -> bookRepository.addBook(bookDto));

        String nameParam = "a";
        String response = RestAssured
                .given()
                    .queryParam("name", nameParam)
                .when()
                    .get("/get-books")
                .then()
                    .statusCode(200)
                    .extract()
                    .body()
                    .asString();

        Mockito.verify(bookRepository).getAllBooks();

        List<BookDto> responseList = Arrays.asList(objectMapper.readValue(response, BookDto[].class));
        Assertions.assertThat(responseList)
                .containsExactlyInAnyOrderElementsOf(
                        bookList.stream()
                                .filter(bookDto ->
                                        bookDto.title().contains(nameParam) || bookDto.isbn().contains(nameParam)
                                )
                                .collect(Collectors.toList())
                );
    }
}