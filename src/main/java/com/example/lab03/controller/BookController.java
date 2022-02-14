package com.example.lab03.controller;

import com.example.lab03.dto.BookDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BookController {

    @GetMapping("/create-book")
    public String createBookForm() {
        return "create-book";
    }

    @PostMapping("/create-book")
    public String createBookConfirm(BookDto bookDto, Model model) {
        model.addAttribute("book", bookDto);
        return "books";
    }

}
