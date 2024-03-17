$(document).ready(function () {

    $('.block-job').on('click', function (e) {
        e.preventDefault();
        var jobId = $(this).data('job-id');
        var $row = $(this).closest('tr');

        $.ajax({
            url: '/admin/jobs/block/' + jobId,
            method: 'POST',
            success: function (response) {

                $row.find('.job-status .text-success').text('Deleted').removeClass('text-success').addClass('text-danger');
                $row.find('.block-job').hide();

                console.log("Job blocked successfully");
            },
            error: function (xhr, status, error) {
                console.error(error);
            }
        });
    });

    // Click event handler for unlocking job
    $('.unlock-job').on('click', function (e) {
        e.preventDefault();
        var jobId = $(this).data('job-id');
        var $row = $(this).closest('tr');

        $.ajax({
            url: '/admin/jobs/unlock/' + jobId,
            method: 'POST',
            success: function (response) {

                $row.find('.job-status .text-danger').text('Active').removeClass('text-danger').addClass('text-success');

                $row.find('.unlock-job').hide();
                console.log("Job unlocked successfully");
            },
            error: function (xhr, status, error) {
                console.error(error);
            }
        });
    });
});
