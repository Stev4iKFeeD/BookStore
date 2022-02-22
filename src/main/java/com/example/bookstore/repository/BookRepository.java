package com.example.bookstore.repository;

import com.example.bookstore.dto.BookDto;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public final class BookRepository {
    private static final List<BookDto> BOOK_REPOSITORY = new ArrayList<>();

    public void addBook(BookDto bookDto) {
        BOOK_REPOSITORY.add(bookDto);
    }

    public List<BookDto> getAllBooks() {
        return new ArrayList<>(BOOK_REPOSITORY);
    }
}
