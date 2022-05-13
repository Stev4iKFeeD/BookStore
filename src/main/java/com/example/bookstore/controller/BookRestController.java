package com.example.bookstore.controller;

import com.example.bookstore.Defaults;
import com.example.bookstore.IsbnValidator;
import com.example.bookstore.dto.BookDto;
import com.example.bookstore.dto.UserSignUpDto;
import com.example.bookstore.entity.BookEntity;
import com.example.bookstore.entity.UserEntity;
import com.example.bookstore.security.MyPasswordEncoder;
import com.example.bookstore.service.BookService;
import com.example.bookstore.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;

@RestController
public class BookRestController {

    private final BookService bookService;
    private final UserService userService;
    private final HttpServletRequest httpServletRequest;
    private final MyPasswordEncoder myPasswordEncoder;

    private final ObjectMapper objectMapper;

    public BookRestController(BookService bookService, UserService userService, HttpServletRequest httpServletRequest,
                              MyPasswordEncoder myPasswordEncoder, ObjectMapper objectMapper) {
        this.bookService = bookService;
        this.userService = userService;
        this.httpServletRequest = httpServletRequest;
        this.myPasswordEncoder = myPasswordEncoder;
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
    public ResponseEntity<BookDto> createBook(
            @Valid @RequestBody BookDto bookDto
    ) throws MethodArgumentNotValidException {
        if (!IsbnValidator.validate(bookDto.getIsbn())) {
            BindingResult bindingResult = new MapBindingResult(new HashMap<>(), "bookDto");
            bindingResult.addError(new FieldError("bookDto", "isbn", "ISBN is not valid"));
            throw new MethodArgumentNotValidException(null, bindingResult);
        }

        BookEntity returnedBookEntity = bookService.createBook(
                new BookEntity(bookDto.getIsbn(), bookDto.getTitle(), bookDto.getAuthor()));
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new BookDto(
                        returnedBookEntity.getIsbn(), returnedBookEntity.getTitle(), returnedBookEntity.getAuthor()
                ));
    }

    @RequestMapping(value = "/favourites", method = RequestMethod.GET)
    public ResponseEntity<String> getIsFavourite(
            @RequestParam(value = "isbn", required = false) String isbn,
            Principal principal
    ) {
        try {
            UserEntity userEntity = userService.getUserWithFavouritesByLogin(principal.getName());
            if (userEntity == null) {
                return ResponseEntity.notFound().build();
            } else {
                if (isbn == null) {
                    return ResponseEntity.ok(
                            objectMapper.writeValueAsString(
                                    userEntity.getFavourites().stream().map(bookEntity -> new BookDto(
                                            bookEntity.getIsbn(), bookEntity.getTitle(), bookEntity.getAuthor()
                                    )).toList()
                            )
                    );
                } else {
                    BookEntity bookInFavourite = userService.getFavouriteByLogin(principal.getName(), isbn);
                    if (bookInFavourite == null) {
                        return ResponseEntity.ok(objectMapper.writeValueAsString(Collections.EMPTY_LIST));
                    } else {
                        return ResponseEntity.ok(objectMapper.writeValueAsString(Collections.singletonList(
                                new BookDto(
                                        bookInFavourite.getIsbn(),
                                        bookInFavourite.getTitle(),
                                        bookInFavourite.getAuthor()
                                )
                        )));
                    }
                }
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @RequestMapping(value = "/favourites", method = RequestMethod.POST)
    public ResponseEntity<String> addToFavourite(
            @RequestParam(value = "isbn") String isbn,
            Principal principal
    ) {
        if (userService.addToFavouritesByLogin(principal.getName(), bookService.getBookByIsbn(isbn))) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(value = "/favourites", method = RequestMethod.DELETE)
    public ResponseEntity<String> removeFromFavourite(
            @RequestParam(value = "isbn") String isbn,
            Principal principal
    ) {
        if (userService.removeFromFavouritesByLogin(principal.getName(), bookService.getBookByIsbn(isbn))) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ResponseEntity<String> signUp(
            @Valid @RequestBody UserSignUpDto userSignUpDto
    ) {
        if (userService.existsUserByLogin(userSignUpDto.getLogin())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } else {
            String rawPassword = userSignUpDto.getPassword();
            UserEntity registeredUser = userService.createUser(
                    userSignUpDto.getLogin(),
                    myPasswordEncoder.encode(rawPassword),
                    userSignUpDto.getName()
            );
            if (registeredUser == null) {
                return ResponseEntity.badRequest().build();
            } else {
                try {
                    httpServletRequest.login(registeredUser.getLogin(), rawPassword);
                } catch (ServletException e) {
                    throw new RuntimeException(e);
                }
                return ResponseEntity.ok().build();
            }
        }
    }

}
