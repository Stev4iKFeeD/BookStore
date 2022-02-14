package com.example.lab03.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BookController {

    @GetMapping("/create-book")
    public String createBookForm() {
        return "create-book";
    }

}
