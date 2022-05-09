package com.example.bookstore.controller;

import com.example.bookstore.Defaults;
import com.example.bookstore.entity.BookEntity;
import com.example.bookstore.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class BookRestController {

    private final BookService bookService;

    private final ObjectMapper objectMapper;

    public BookRestController(BookService bookService, ObjectMapper objectMapper) {
        this.bookService = bookService;
        this.objectMapper = objectMapper;
    }

    @RequestMapping(value = "/get-books", method = RequestMethod.GET)
    public ResponseEntity<ObjectNode> getFilteredBooks(
            @RequestParam(value = "filter", required = false) String filter,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "page-size", required = false) Integer pageSize
    ) {
        Page<BookEntity> bookPage;
        Pageable pageable = Defaults.createPageable(page, pageSize);

        if (filter == null) {
            bookPage = bookService.getAllBooks(pageable);
        } else {
            bookPage = bookService.getBookByFilter(filter, pageable);
        }

        ObjectNode responseObject = objectMapper.createObjectNode();

        ArrayNode dataArray = responseObject.putArray("data");
        for (BookEntity book : bookPage) {
            dataArray.add(objectMapper.valueToTree(book));
        }

        responseObject.put("hasPrevious", bookPage.hasPrevious());
        responseObject.put("hasNext", bookPage.hasNext());

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(responseObject);
    }

    @RequestMapping(value = "/create-book", method = RequestMethod.POST)
    public ResponseEntity<BookEntity> createBook(
            @RequestBody BookEntity bookEntity
    ) {
        BookEntity returnedBookEntity = bookService.createBook(bookEntity);
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(returnedBookEntity);
    }

}
