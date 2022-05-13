package com.example.bookstore.entity;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "book")
public class BookEntity {

    @Id
    @Column(name = "isbn")
    private String isbn;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "author")
    private String author;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "favourites")
    private List<UserEntity> usersInFavourites;

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public BookEntity() {

    }

    public BookEntity(String isbn, String title, String author) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookEntity that = (BookEntity) o;
        return Objects.equals(isbn, that.isbn)
                && Objects.equals(title, that.title)
                && Objects.equals(author, that.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn, title, author);
    }

    @Override
    public String toString() {
        return "BookEntity{" +
                "isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                '}';
    }

}
