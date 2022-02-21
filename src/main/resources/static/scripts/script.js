$(function () {
    $('#filters-form').submit(function (e) {
        e.preventDefault();

        let $filter = $('#filter-name').val();
        $.ajax({
            type: 'GET',
            url: '/get-books?name=' + $filter,
            success: function (response) {
                let $table = $('#books-table-body');
                $table.empty();
                response.forEach(function (book) {
                    $table.append(
                        '<tr>' +
                        '<td>' + book.title + '</td>' +
                        '<td>' + book.isbn + '</td>' +
                        '<td>' + book.title + '</td>' +
                        '</tr>');
                });
            }
        });
    });
});