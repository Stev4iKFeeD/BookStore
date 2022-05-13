package com.example.bookstore.controller;

import com.example.bookstore.dto.BookDto;
import com.example.bookstore.dto.UserDto;
import com.example.bookstore.entity.BookEntity;
import com.example.bookstore.entity.UserEntity;
import com.example.bookstore.service.BookService;
import com.example.bookstore.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@Controller
public class BookController {

    private final BookService bookService;
    private final UserService userService;

    public BookController(BookService bookService, UserService userService) {
        this.bookService = bookService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String redirectToAllBooks() {
        return "redirect:/books";
    }

    @GetMapping("/books")
    public String allBooks() {
        return "books";
    }

    @GetMapping("/books/{isbn}")
    public String bookByIsbn(@PathVariable String isbn, Model model) {
        BookEntity bookEntity = bookService.getBookByIsbn(isbn);
        if (bookEntity == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No book with isbn '" + isbn + "'");
        } else {
            model.addAttribute("book", new BookDto(
                    bookEntity.getIsbn(), bookEntity.getTitle(), bookEntity.getAuthor()
            ));
            return "book-page";
        }
    }

    @GetMapping("/user")
    public String userPage(Model model, Principal principal) {
        UserEntity userEntity = userService.getUserWithFavouritesByLogin(principal.getName());
        model.addAttribute("user", new UserDto(userEntity.getLogin(), userEntity.getName()));
        return "user";
    }

    @GetMapping("/signup")
    public String signUp() {
        return "signup";
    }

}
