package com.example.bookstore.controller;

import com.example.bookstore.dto.BookDto;
import com.example.bookstore.repository.BookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class BookRestController {

    private final BookRepository BOOK_REPOSITORY;

    public BookRestController(BookRepository BOOK_REPOSITORY) {
        this.BOOK_REPOSITORY = BOOK_REPOSITORY;
    }

    @RequestMapping(value = "/get-books", method = RequestMethod.GET)
    public ResponseEntity<List<BookDto>> getFilteredBooks(
            @RequestParam(value = "name", required = false) String name
    ) {
        List<BookDto> booksToReturn = BOOK_REPOSITORY.getAllBooks();
        if (name != null) {
            booksToReturn = booksToReturn
                    .stream()
                    .filter(bookDto -> bookDto.title().contains(name) || bookDto.isbn().contains(name))
                    .collect(Collectors.toList());
        }

        return ResponseEntity.ok(booksToReturn);
    }

    @RequestMapping(value = "/create-book", method = RequestMethod.POST)
    public ResponseEntity<BookDto> createBook(
            @RequestBody BookDto bookDto
    ) {
        BOOK_REPOSITORY.addBook(bookDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(bookDto);
    }

}
