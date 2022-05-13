package com.example.bookstore;

public class IsbnValidator {
    public static boolean validate(String isbn) {
        if (isbn.length() != 13) {
            return false;
        }
        try {
            long isbnLong = Long.parseLong(isbn);
            return isbnSum(isbnLong) % 10 == 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static int isbnSum(long isbn) {
        int sum = 0;
        for (int count = 1; isbn > 0; count++) {
            sum += count % 2 == 0 ? 3 * (isbn % 10) : isbn % 10;
            isbn /= 10;
        }
        return sum;
    }
}
