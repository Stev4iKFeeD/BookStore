package com.example.bookstore.service;

import com.example.bookstore.entity.BookEntity;
import com.example.bookstore.repository.BookJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class BookService {

    private final BookJpaRepository bookRepository;

    public BookService(BookJpaRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Transactional
    public BookEntity createBook(BookEntity bookEntity) {
        return bookRepository.saveAndFlush(bookEntity);
    }

    public BookEntity createBook(String isbn, String title, String author) {
        return createBook(new BookEntity(isbn, title, author));
    }

    @Transactional
    public Page<BookEntity> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    @Transactional
    public BookEntity getBookByIsbn(String isbn) {
        return bookRepository.findById(isbn).orElse(null);
    }

    @Transactional
    public Page<BookEntity> getBookByFilter(String filter, Pageable pageable) {
        return bookRepository.findByIsbnOrTitle("%" + filter + "%", pageable);
    }

}
