$(document).ready(function () {
    $('.delete-applicantlist').on('click', function () {
        var applicantListId = $(this).data('applicantlist-id');
        var applicantListDiv = $(this).closest('.single-row');

        $.ajax({
            url: '/applicant-list/delete/' + applicantListId,
            method: 'DELETE',
            success: function (response) {
                applicantListDiv.remove();
                console.log("ApplicantList deleted successfully");
            },
            error: function (xhr, status, error) {
                console.error(xhr.responseText);
                console.error(error);
            }
        });
    });
});
