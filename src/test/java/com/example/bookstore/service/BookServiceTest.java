package com.example.bookstore.service;

import com.example.bookstore.Defaults;
import com.example.bookstore.MySpringTestListeners;
import com.example.bookstore.entity.BookEntity;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@SpringBootTest
@MySpringTestListeners
@DatabaseSetup("/service/BookService/init.xml")
@DatabaseTearDown("/service/BookService/clean-up.xml")
class BookServiceTest {

    @Autowired
    private BookService bookService;

    private Pageable pageable = Defaults.createPageable(null, null);

    @Test
    @ExpectedDatabase(value = "/service/BookService/expected-after-create.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void createBookTest() {
        BookEntity bookEntity = new BookEntity("isbnnew", "titlenew", "authornew");
        bookService.createBook(bookEntity);
        Assertions.assertThat(bookService.getAllBooks(pageable))
                .hasSize(5);
    }

    @Test
    void getAllBooksTest() {
        List<BookEntity> bookEntities = bookService.getAllBooks(pageable).toList();
        Assertions.assertThat(bookEntities)
                .hasSize(4);
    }

    @Test
    void getBookByIsbnTest() {
        Assertions.assertThat(bookService.getBookByIsbn("isbn1"))
                .isEqualTo(new BookEntity("isbn1", "title1", "author1"));

        Assertions.assertThat(bookService.getBookByIsbn("noisbn"))
                .isNull();
    }

    @Test
    void getBookByFilterTest() {
        Assertions.assertThat(bookService.getBookByFilter("isbn", pageable))
                .hasSize(4);

        Assertions.assertThat(bookService.getBookByFilter("isbn1", pageable))
                .hasSize(2);
        Assertions.assertThat(bookService.getBookByFilter("title1", pageable))
                .hasSize(2);
//        Assertions.assertThat(bookService.getBookByFilter("author1", pageable))
//                .hasSize(2);

        Assertions.assertThat(bookService.getBookByFilter("nofilter", pageable))
                .isEmpty();

    }

}