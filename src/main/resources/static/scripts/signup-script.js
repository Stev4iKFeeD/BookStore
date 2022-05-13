$(function () {
    $('#sign-up-form').submit(function (e) {
        e.preventDefault();
        $('#sign-up-user-already-exists').remove();

        $.ajax({
            type: 'POST',
            url: '/signup',
            dataType: 'json',
            data: JSON.stringify({
                login: $(this).find('[name=login]').val(),
                password: $(this).find('[name=password]').val(),
                name: $(this).find('[name=name]').val()
            }),
            beforeSend: function (xhr) {
                xhr.setRequestHeader('Content-Type', 'application/json')
            },
            statusCode: {
                409: function () {
                    $('#sign-up-form').prepend(
                        "<h3 id='sign-up-user-already-exists'>Login is already taken</h3>"
                    );
                },
                200: function () {
                    document.location.href = '/';
                }
            }
        });
    });
});
