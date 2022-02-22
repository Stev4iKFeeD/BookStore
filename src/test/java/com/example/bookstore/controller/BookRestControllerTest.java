package com.example.bookstore.controller;

import com.example.bookstore.dto.BookDto;
import com.example.bookstore.repository.BookRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.assertj.core.api.Assertions;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookRestControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookRepository bookRepository;

    @LocalServerPort
    void setPort(int port) {
        RestAssured.port = port;
    }

    @Test
    void createBookTest() throws IOException {
        Mockito.doNothing().when(bookRepository).addBook(ArgumentMatchers.any());
        JsonNode jsonNode = objectMapper.readTree(
                BookRestControllerTest.class.getResourceAsStream("/createBookTestData.json")
        );
        String jsonString = jsonNode.toString();

        RestAssured
                .given()
                    .contentType(ContentType.JSON)
                    .body(jsonString)
                .when()
                    .post("/create-book")
                .then()
                    .statusCode(201)
                    .body("title", CoreMatchers.is(jsonNode.get("title").asText()))
                    .body("isbn", CoreMatchers.is(jsonNode.get("isbn").asText()))
                    .body("author", CoreMatchers.is(jsonNode.get("author").asText()));

        Mockito.verify(bookRepository).addBook(ArgumentMatchers.any());
    }

    @Test
    void getBooksWithoutFiltersTest() throws JsonProcessingException {
        List<BookDto> bookList = Arrays.asList(
                new BookDto("aaa", "bbb", "aaa"),
                new BookDto("ccc", "ddd", "bbb"),
                new BookDto("eee", "eee", "ccc"),
                new BookDto("aab", "aab", "aab")
        );
        Mockito.doReturn(bookList).when(bookRepository).getAllBooks();

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
    void getBooksWithFiltersTest() throws JsonProcessingException {
        List<BookDto> bookList = Arrays.asList(
                new BookDto("aaa", "bbb", "aaa"),
                new BookDto("ccc", "ddd", "bbb"),
                new BookDto("eee", "eee", "ccc"),
                new BookDto("aab", "aab", "aab")
        );
        Mockito.doReturn(bookList).when(bookRepository).getAllBooks();

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