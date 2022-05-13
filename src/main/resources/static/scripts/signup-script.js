$(function () {
    $('#sign-up-form').submit(function (e) {
        e.preventDefault();
        $('.sign-up-error').remove();

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
                400: function (response) {
                    let $form = $('#sign-up-form');
                    response.responseJSON.forEach(function (error) {
                        $form.prepend(
                            "<h3 class='sign-up-error'>" + error +"</h3>"
                        );
                    });
                },
                409: function () {
                    $('#sign-up-form').prepend(
                        "<h3 id='sign-up-error'>Login is already taken</h3>"
                    );
                },
                200: function () {
                    document.location.href = '/';
                }
            }
        });
    });
});
