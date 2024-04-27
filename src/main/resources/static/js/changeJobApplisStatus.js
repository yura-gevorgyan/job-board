$(document).ready(function () {

    $('.approve-job').on('click', function (e) {
        e.preventDefault();
        var jobApplyId = $(this).data('jobapply-id');
        var $row = $(this).closest('tr');

        $.ajax({
            url: '/job-applies/approve/' + jobApplyId,
            method: 'POST',
            success: function (response) {
                $row.find('.jobapplies-status .text-warning').text('Approved').removeClass('text-warning').addClass('text-success');
                $row.find('.reject-job').hide();
                $row.find('.approve-job').hide();
            },
            error: function (xhr, status, error) {
                console.error(error);
            }
        });
    });

    $('.reject-job').on('click', function (e) {
        e.preventDefault();
        var jobApplyId = $(this).data('jobapply-id');
        var $row = $(this).closest('tr');

        $.ajax({
            url: '/job-applies/reject/' + jobApplyId,
            method: 'POST',
            success: function (response) {
                $row.find('.jobapplies-status .text-warning').text('Rejected').removeClass('text-warning').addClass('text-danger');
                $row.find('.reject-job').hide();
                $row.find('.approve-job').hide();
            },
            error: function (xhr, status, error) {
                console.error(error);
            }
        });
    });
});
