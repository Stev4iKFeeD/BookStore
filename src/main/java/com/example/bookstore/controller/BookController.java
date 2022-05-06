package com.example.bookstore.controller;

import com.example.bookstore.entity.BookEntity;
import com.example.bookstore.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public String redirectToAllBooks() {
        return "redirect:/books";
    }

    @GetMapping("/books")
    public String allBooks() {
        return "books";
    }

    @GetMapping("/books/{isbn}")
    public String bookByIsbn(@PathVariable String isbn, Model model) {
        BookEntity bookToReturn = bookService.getBookByIsbn(isbn);
        if (bookToReturn == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No book with isbn '" + isbn + "'");
        } else {
            model.addAttribute("book", bookToReturn);
            return "book-page";
        }
    }

}
