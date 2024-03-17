$(document).ready(function () {
    $('.delete-company').on('click', function () {
        var companyId = $(this).data('company-id');
        var companyDiv = $(this).closest('.single-row');

        $.ajax({
            url: '/admin/companies/delete/' + companyId,
            method: 'DELETE',
            success: function (response) {
                companyDiv.remove();
                console.log("Company deleted successfully");
            },
            error: function (xhr, status, error) {
                console.error(xhr.responseText);
                console.error(error);
            }
        });
    });
});