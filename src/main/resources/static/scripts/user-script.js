$(function () {
    function addClickableLogic() {
        $('.clickable-row').each(function () {
            let $row = $(this);
            $row.click(function () {
                let $isbn = $row.children().eq(0).html();
                document.location.href = '/books/' + $isbn;
            });
        });
    }

    function updateFavouritesTable(bookList) {
        let $table = $('#favourites-table-body');
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

    function loadFavourites() {
        $.ajax({
            type: 'GET',
            url: '/favourites',
            success: function (response) {
                updateFavouritesTable($.parseJSON(response));
            }
        });
    }

    loadFavourites();
});
