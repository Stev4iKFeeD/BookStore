package com.example.bookstore;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class Defaults {
    public static final int PAGE_SIZE = 10;
    public static final int MAX_PAGE_SIZE = 50;

    public static Pageable createPageable(Integer page, Integer pageSize) {
        Pageable pageable;

        if (page == null && pageSize == null) {
            pageable = PageRequest.of(0, Defaults.PAGE_SIZE);
        } else if (page == null) {
            pageable = PageRequest.of(0, Math.min(pageSize, Defaults.MAX_PAGE_SIZE));
        } else if (pageSize == null) {
            pageable = PageRequest.of(page, Defaults.PAGE_SIZE);
        } else {
            pageable = PageRequest.of(page, Math.min(pageSize, Defaults.MAX_PAGE_SIZE));
        }

        return pageable;
    }
}
