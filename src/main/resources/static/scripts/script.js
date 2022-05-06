$(function () {
    let currentFilter = '';

    function addClickableLogic() {
        $('.clickable-row').each(function () {
            let $row = $(this);
            $row.click(function () {
                let $isbn = $row.children().eq(0).html();
                document.location.href = '/books/' + $isbn;
            })
        });
    }

    function updateBookTable(bookList) {
        let $table = $('#books-table-body');
        $table.empty();
        bookList.forEach(function (book) {
            $table.append(
                '<tr class="clickable-row">' +
                '<td>' + book.isbn + '</td>' +
                '<td>' + book.title + '</td>' +
                '<td>' + book.author + '</td>' +
                '</tr>');
        });
        addClickableLogic();
    }

    function loadBooks() {
        $.ajax({
            type: 'GET',
            url: '/get-books?filter=' + currentFilter,
            success: function (response) {
                updateBookTable(response)
            }
        });
    }

    function createBook(isbn, title, author) {
        $.ajax({
            type: 'POST',
            url: '/create-book',
            dataType: 'json',
            data: JSON.stringify({
                isbn: isbn,
                title: title,
                author: author
            }),
            beforeSend: function (xhr) {
                xhr.setRequestHeader('Content-Type', 'application/json')
            },
            success: function (response) {
                /**
                 * Creating book without getting all books again
                 */

                // let $isbn = response.isbn;
                // let $title = response.title;
                // let $author = response.author;
                //
                // if (currentFilter ^ ($isbn.includes(currentFilter) || $title.includes(currentFilter) || $author.includes(currentFilter))) {
                //     $('#books-table-body').append(
                //         '<tr>' +
                //         '<td>' + $isbn + '</td>' +
                //         '<td>' + $title + '</td>' +
                //         '<td>' + $author + '</td>' +
                //         '</tr>'
                //     );
                // }

                /////////////////////////////////////////////////////////////////////////////////////

                loadBooks();
            }
        });
    }

    $('#create-book-form').submit(function (e) {
        e.preventDefault();

        let $isbnField = $(this).find('[name=isbn]');
        let $titleField = $(this).find('[name=title]');
        let $authorField = $(this).find('[name=author]');

        createBook($isbnField.val(), $titleField.val(), $authorField.val());

        $(this)[0].reset();
    });

    {
        let $filtersForm = $('#filters-form');
        $filtersForm.submit(function (e) {
            e.preventDefault();

            currentFilter = $(this).find('[name=filter]').val();
            loadBooks();
        });
        $filtersForm.eq(0).on('reset', function () {
            currentFilter = '';
            loadBooks();
        });
    }

    loadBooks();
});
