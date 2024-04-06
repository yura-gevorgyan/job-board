document.addEventListener('change', function(event) {
    if (event.target && event.target.id === 'picName') {
        var file = event.target.files[0];
        if (file) {
            var reader = new FileReader();
            reader.onload = function(e) {
                var previewImage = document.querySelector('.border-radius-50');
                previewImage.setAttribute('src', e.target.result);
            }
            reader.readAsDataURL(file);
        }
    }
});
