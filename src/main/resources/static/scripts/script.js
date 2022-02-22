$(function () {
    let currentName = '';

    function updateBookTable(bookList) {
        let $table = $('#books-table-body');
        $table.empty();
        bookList.forEach(function (book) {
            $table.append(
                '<tr>' +
                '<td>' + book.title + '</td>' +
                '<td>' + book.isbn + '</td>' +
                '<td>' + book.author + '</td>' +
                '</tr>');
        });
    }

    function loadBooks() {
        $.ajax({
            type: 'GET',
            url: '/get-books?name=' + currentName,
            success: function (response) {
                updateBookTable(response)
            }
        });
    }

    function createBook(title, isbn, author) {
        $.ajax({
            type: 'POST',
            url: '/create-book',
            dataType: 'json',
            data: JSON.stringify({
                title: title,
                isbn: isbn,
                author: author
            }),
            beforeSend: function (xhr) {
                xhr.setRequestHeader('Content-Type', 'application/json')
            },
            success: function (response) {
                /**
                 * Creating book without getting all books again
                 */

                // let $title = response.title;
                // let $isbn = response.isbn;
                // let $author = response.author;
                //
                // if (currentName ^ ($title.includes(currentName) || $isbn.includes(currentName))) {
                //     $('#books-table-body').append(
                //         '<tr>' +
                //         '<td>' + $title + '</td>' +
                //         '<td>' + $isbn + '</td>' +
                //         '<td>' + $author + '</td>' +
                //         '</tr>'
                //     );
                // }

                /////////////////////////////////////////////////////////////////////////////////////

                loadBooks();
            }
        });
    }

    $('#filters-form').submit(function (e) {
        e.preventDefault();

        currentName = $(this).find('[name=name]').val();
        loadBooks();
    });

    $('#create-book-form').submit(function (e) {
        e.preventDefault();

        let $title = $(this).find('[name=title]').val();
        let $isbn = $(this).find('[name=isbn]').val();
        let $author = $(this).find('[name=author]').val();
        createBook($title, $isbn, $author);
    });
});
