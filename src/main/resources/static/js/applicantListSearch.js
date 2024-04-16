    $(document).ready(function() {
    $('#search').on('input', function () {
        var searchText = $(this).val().toLowerCase();
        $('tbody tr').each(function () {
            var title = $(this).find('td:first').text().toLowerCase();
            var date = $(this).find('td:nth-child(2)').text().toLowerCase();
            var status = $(this).find('td:nth-child(3)').text().toLowerCase();
            if (title.includes(searchText) || date.includes(searchText) || status.includes(searchText)) {
                $(this).show();
            } else {
                $(this).hide();
            }
        });
    });
});
