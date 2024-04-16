$(document).ready(function () {
    $('.delete-applicantList').on('click', function () {
        var applicantListId = $(this).data('applicantList-id');
        console.log("ApplicantListId:", applicantListId);
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
