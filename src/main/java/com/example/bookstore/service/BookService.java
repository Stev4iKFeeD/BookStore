package com.example.bookstore.service;

import com.example.bookstore.entity.BookEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class BookService {

    private final EntityManager entityManager;

    public BookService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public BookEntity createBook(BookEntity bookEntity) {
        return entityManager.merge(bookEntity);
    }

    @Transactional
    public List<BookEntity> getAllBooks() {
        return entityManager.createQuery("SELECT b FROM BookEntity b", BookEntity.class).getResultList();
    }

    @Transactional
    public BookEntity getBookByIsbn(String isbn) {
        return entityManager.find(BookEntity.class, isbn);
    }

    @Transactional
    public List<BookEntity> getBookByFilter(String filter) {
        return entityManager.createQuery(
                        """
                                SELECT b
                                FROM BookEntity b
                                WHERE b.isbn LIKE :filter OR
                                      b.title LIKE :filter OR
                                      b.author LIKE :filter
                                """, BookEntity.class
                ).setParameter("filter", "%" + filter + "%")
                .getResultList();
    }

}
