package com.example.bookstore.repository;

import com.example.bookstore.entity.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookJpaRepository extends JpaRepository<BookEntity, String> {

    @Query("""
            SELECT b
            FROM BookEntity b
            WHERE b.isbn LIKE :filter OR
                  b.title LIKE :filter
            """)
    Page<BookEntity> findByIsbnOrTitle(@Param("filter") String filter, Pageable pageable);
}
