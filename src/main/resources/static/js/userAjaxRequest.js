$(document).ready(function () {

    $('.block-user').on('click', function (e) {
        e.preventDefault();
        var userId = $(this).data('user-id');
        var $row = $(this).closest('tr');

        $.ajax({
            url: '/admin/users/block/' + userId,
            method: 'POST',
            success: function (response) {

                $row.find('.user-status .text-success').text('Deleted').removeClass('text-success').addClass('text-danger');
                $row.find('.block-user').hide();

                console.log("User blocked successfully");
            },
            error: function (xhr, status, error) {
                console.error(error);
            }
        });
    });

    // Click event handler for unlocking job
    $('.unlock-user').on('click', function (e) {
        e.preventDefault();
        var userId = $(this).data('user-id');
        var $row = $(this).closest('tr');

        $.ajax({
            url: '/admin/users/unlock/' + userId,
            method: 'POST',
            success: function (response) {

                $row.find('.user-status .text-danger').text('Active').removeClass('text-danger').addClass('text-success');

                $row.find('.unlock-user').hide();
                console.log("User unlocked successfully");
            },
            error: function (xhr, status, error) {
                console.error(error);
            }
        });
    });
});
