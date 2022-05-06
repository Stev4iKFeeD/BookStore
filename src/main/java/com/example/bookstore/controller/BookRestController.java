package com.example.bookstore.controller;

import com.example.bookstore.entity.BookEntity;
import com.example.bookstore.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class BookRestController {

    private final BookService bookService;

    public BookRestController(BookService bookService) {
        this.bookService = bookService;
    }

    @RequestMapping(value = "/get-books", method = RequestMethod.GET)
    public ResponseEntity<List<BookEntity>> getFilteredBooks(
            @RequestParam(value = "filter", required = false) String filter
    ) {
        List<BookEntity> booksToReturn;
        if (filter == null) {
            booksToReturn = bookService.getAllBooks();
        } else {
            booksToReturn = bookService.getBookByFilter(filter);
        }

        return ResponseEntity.ok(booksToReturn);
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
