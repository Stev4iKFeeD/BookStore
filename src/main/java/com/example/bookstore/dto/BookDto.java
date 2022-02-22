package com.example.bookstore.dto;

import java.util.Objects;

public record BookDto(String title, String isbn, String author) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookDto bookDto = (BookDto) o;
        return Objects.equals(title, bookDto.title) && Objects.equals(isbn, bookDto.isbn) && Objects.equals(author, bookDto.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, isbn, author);
    }
}
