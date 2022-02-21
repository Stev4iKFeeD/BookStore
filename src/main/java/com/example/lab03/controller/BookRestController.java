package com.example.lab03.controller;

import com.example.lab03.dto.BookDto;
import com.example.lab03.repository.BookRepository;
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

}
