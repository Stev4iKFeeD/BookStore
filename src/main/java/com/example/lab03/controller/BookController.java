package com.example.lab03.controller;

import com.example.lab03.repository.BookRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BookController {

    private final BookRepository BOOK_REPOSITORY;

    public BookController(BookRepository BOOK_REPOSITORY) {
        this.BOOK_REPOSITORY = BOOK_REPOSITORY;
    }

    @GetMapping
    public String redirectToAllBooks() {
        return "redirect:/books";
    }

    @GetMapping("/books")
    public String allBooks(Model model) {
        model.addAttribute("books", BOOK_REPOSITORY.getAllBooks());
        return "books";
    }

}
