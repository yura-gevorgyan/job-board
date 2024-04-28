document.addEventListener('change', function (event) {
    if (event.target && event.target.id === 'picNamee') {
        let file = event.target.files[0];
        if (file) {
            let reader = new FileReader();
            reader.onload = function (e) {
                let previewImage = document.querySelector('.border-radius-50');
                previewImage.setAttribute('src', e.target.result);
            }
            reader.readAsDataURL(file);
        }
    }
});

document.addEventListener('change', function (event) {
    if (event.target && event.target.id === 'logo') {
        let file = event.target.files[0];
        if (file) {
            let reader = new FileReader();
            reader.onload = function (e) {
                let previewImage = document.querySelector('.border-radius-50');
                previewImage.setAttribute('src', e.target.result);
            }
            reader.readAsDataURL(file);
        }
    }
});