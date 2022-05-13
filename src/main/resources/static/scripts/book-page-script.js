$(function () {
    function addClickableLogic() {
        $('.not-in-favourites').each(function () {
            $(this).unbind().click();
            $(this).click(() => addToFavourites());
        });

        $('.in-favourites').each(function () {
            $(this).unbind().click();
            $(this).click(() => removeFromFavourites());
        });
    }

    function getFromFavourites() {
        $.ajax({
            type: 'GET',
            url: '/favourites',
            data: {
                'isbn': isbn
            },
            success: function (response) {
                if ($.parseJSON(response).length !== 0) {
                    $('.not-in-favourites').each(function () {
                        let $fav = $(this);
                        $fav.removeClass('not-in-favourites');
                        $fav.addClass('in-favourites');
                        $fav.unbind().click();
                        $fav.click(() => removeFromFavourites());
                    });
                }
            }
        });
    }

    function addToFavourites() {
        $.ajax({
            type: 'POST',
            url: '/favourites',
            data: {
                'isbn': isbn
            },
            success: function () {
                $('.not-in-favourites').each(function () {
                    let $fav = $(this);
                    $fav.removeClass('not-in-favourites');
                    $fav.addClass('in-favourites');
                    $fav.unbind().click();
                    $fav.click(() => removeFromFavourites());
                });
            }
        });
    }

    function removeFromFavourites() {
        $.ajax({
            type: 'DELETE',
            url: '/favourites',
            data: {
                'isbn': isbn
            },
            success: function () {
                $('.in-favourites').each(function () {
                    let $fav = $(this);
                    $fav.removeClass('in-favourites');
                    $fav.addClass('not-in-favourites');
                    $fav.unbind().click();
                    $fav.click(() => addToFavourites());
                });
            }
        });
    }

    getFromFavourites();
    addClickableLogic();
});
