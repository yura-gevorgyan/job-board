$(document).ready(function () {
    $('.delete-resume').on('click', function () {
        var resumeId = $(this).data('resume-id');
        var resumeDiv = $(this).closest('.card-style2');

        $.ajax({
            url: '/admin/resumes/delete/' + resumeId,
            method: 'DELETE',
            success: function (response) {
                resumeDiv.remove();
                console.log("Resume deleted successfully");
            },
            error: function (xhr, status, error) {
                console.error(xhr.responseText);
                console.error(error);
            }
        });
    });
});
