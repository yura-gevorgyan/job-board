$(document).ready(function () {

    $('.wishList').on('click', function (e) {
        e.preventDefault();
        console.log("aaaaa")
        var jobId = $(this).data('job-id');
        var $row = $(this).closest('a');

        if (jobId[0] === 'a') {
            jobId = jobId.slice(1)
            $.ajax({
                url: '/jobs/favorites/add/' + jobId,
                method: 'POST',
                success: function (response) {
                    $row.css('background-color', '#e74c3c');
                    $row.css('color', '#ffffff');
                    $row.data('job-id', jobId);
                },
                error: function (xhr, status, error) {
                    console.error(xhr.responseText);
                    console.error(error);
                }
            });

        } else {
            $.ajax({
                url: '/jobs/favorites/delete/' + jobId,
                method: 'DELETE',
                success: function (response) {
                    $row.css('background-color', '');
                    $row.css('color', '#969696');
                    $row.data('job-id', ('a' + jobId));

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

    $('.delete-favourite-job').on('click', function (e) {
        e.preventDefault();
        var jobId = $(this).data('job-id');
        var $row = $(this).closest('.single-job');

            $.ajax({
                url: '/jobs/favorites/delete/' + jobId,
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
        var jobCards = document.querySelectorAll('.single-job');

        jobCards.forEach(function (jobCard) {
            var jobTitle = jobCard.querySelector('h3').innerText.toLowerCase();
            if (jobTitle.includes(filter)) {
                jobCard.style.display = 'block';
            } else {
                jobCard.style.display = 'none';
            }
        });
    });
})