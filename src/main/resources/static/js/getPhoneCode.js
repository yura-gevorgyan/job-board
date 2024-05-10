$('#country').change(function() {
    var countryId = $(this).val();
    if (countryId !== '0') {
        $.ajax({
            url: '/getPhoneCode/' + countryId,
            type: 'GET',
            data: { countryId: countryId },
            success: function(data) {
                $('#phoneNum').val(data.phoneCode);
            },
            error: function() {
                console.log('Error occurred while fetching phone code.');
            }
        });
    }else {
        $('#phoneNum').val('');
    }
});