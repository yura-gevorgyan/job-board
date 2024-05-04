$(document).ready(function () {

    $('.wishList').on('click', function (e) {
        e.preventDefault();
        console.log("aaaaa")
        var companyId = $(this).data('company-id');
        var $row = $(this).closest('a');

        if (companyId[0] === 'a') {
            companyId = companyId.slice(1)
            $.ajax({
                url: '/companies/favorites/add/' + companyId,
                method: 'POST',
                success: function (response) {
                    $row.css('background-color', '#e74c3c');
                    $row.css('color', '#ffffff');
                    $row.data('company-id', companyId);
                },
                error: function (xhr, status, error) {
                    console.error(xhr.responseText);
                    console.error(error);
                }
            });

        } else {
            $.ajax({
                url: '/companies/favorites/delete/' + companyId,
                method: 'DELETE',
                success: function (response) {
                    $row.css('background-color', '');
                    $row.css('color', '#969696');
                    $row.data('company-id', ('a' + companyId));

                },
                error: function (xhr, status, error) {
                    console.error(xhr.responseText);
                    console.error(error);
                }
            });
        }
    });

});

$(document).ready(function () {

    $('.delete-favourite-company').on('click', function (e) {
        e.preventDefault();
        console.log("aaaaa")
        var companyId = $(this).data('company-id');
        var $row = $(this).closest('.single-company');

        $.ajax({
            url: '/companies/favorites/delete/' + companyId,
            method: 'DELETE',
            success: function (response) {
                $row.remove();
            },
            error: function (xhr, status, error) {
                console.error(xhr.responseText);
                console.error(error);
            }
        });

    });
});

$(function () {
    var searchInput = document.getElementById('search');

    // Add event listener for input
    searchInput.addEventListener('input', function () {
        var filter = searchInput.value.toLowerCase();
        var companyCards = document.querySelectorAll('.single-company');

        companyCards.forEach(function (companyCard) {
            var companyTitle = companyCard.querySelector('h4').innerText.toLowerCase();
            if (companyTitle.includes(filter)) {
                companyCard.style.display = 'block';
            } else {
                companyCard.style.display = 'none';
            }
        });
    });
})
