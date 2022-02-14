package com.example.lab03.controller;

import com.example.lab03.dto.BookDto;
import com.example.lab03.repository.BookRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BookController {

    private final BookRepository BOOK_REPOSITORY;

    public BookController(BookRepository BOOK_REPOSITORY) {
        this.BOOK_REPOSITORY = BOOK_REPOSITORY;
    }

    @GetMapping("/books")
    public String allBooks(Model model) {
        model.addAttribute("books", BOOK_REPOSITORY.getAllBooks());
        return "books";
    }

    @GetMapping("/create-book")
    public String createBookForm() {
        return "create-book";
    }

    @PostMapping("/create-book")
    public String createBookConfirm(BookDto bookDto) {
        BOOK_REPOSITORY.addBook(bookDto);
        return "redirect:books";
    }

}
