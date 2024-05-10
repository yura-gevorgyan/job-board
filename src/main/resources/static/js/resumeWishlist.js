$(document).ready(function () {

    $('.wishList').on('click', function (e) {
        e.preventDefault();
        console.log("aaaaa")
        let resumeId = $(this).data('resume-id');
        let $row = $(this).closest('a');

        if (resumeId[0] === 'a') {
            resumeId = resumeId.slice(1)
            $.ajax({
                url: '/resumes/favorites/add/' + resumeId,
                method: 'POST',
                success: function (response) {
                    $row.css('background-color', '#e74c3c');
                    $row.css('color', '#ffffff');
                    $row.data('resume-id', resumeId);
                },
                error: function (xhr, status, error) {
                    console.error(xhr.responseText);
                    console.error(error);
                }
            });

        } else {
            $.ajax({
                url: '/resumes/favorites/delete/' + resumeId,
                method: 'DELETE',
                success: function (response) {
                    $row.css('background-color', '');
                    $row.css('color', '#969696');
                    $row.data('resume-id', ('a' + resumeId));

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

    $('.delete-favourite-resume').on('click', function (e) {
        e.preventDefault();
        console.log("aaaaa")
        let resumeId = $(this).data('resume-id');
        let $row = $(this).closest('.single-resume');

        $.ajax({
            url: '/resumes/favorites/delete/' + resumeId,
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
    let searchInput = document.getElementById('search');

    // Add event listener for input
    searchInput.addEventListener('input', function () {
        let filter = searchInput.value.toLowerCase();
        let resumeCards = document.querySelectorAll('.single-resume');

        resumeCards.forEach(function (resumeCard) {
            let resumeTitle = resumeCard.querySelector('h4').innerText.toLowerCase();
            if (resumeTitle.includes(filter)) {
                resumeCard.style.display = 'block';
            } else {
                resumeCard.style.display = 'none';
            }
        });
    });
})