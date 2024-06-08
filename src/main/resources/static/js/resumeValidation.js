function validateForm() {
    var isValid = true;

    var pictureInput = document.getElementById('picNamee');
    if (!pictureInput.files || pictureInput.files.length === 0) {
        highlightError(pictureInput);
        showErrorMessage("picNamee", "Picture name cannot be empty");
        isValid = false;
    } else {
        removeErrorHighlight(pictureInput);
        hideErrorMessage("picNamee");
    }

    var professionInput = document.getElementById('profession');
    if (professionInput.value.trim() === "") {
        highlightError(professionInput);
        showErrorMessage("profession", "Profession cannot be empty");
        isValid = false;
    } else {
        removeErrorHighlight(professionInput);
        hideErrorMessage("profession");
    }

    var countrySelect = document.getElementById('country');
    if (countrySelect.value === "0") {
        highlightError(countrySelect);
        showErrorMessage("country", "Please select a country");
        isValid = false;
    } else {
        removeErrorHighlight(countrySelect);
        hideErrorMessage("country");
    }

    var phoneInput = document.getElementById('phoneNum');
    if (phoneInput.value.trim() === "") {
        highlightError(phoneInput);
        showErrorMessage("phoneNum", "Phone number cannot be empty");
        isValid = false;
    } else {
        removeErrorHighlight(phoneInput);
        hideErrorMessage("phoneNum");
    }

    var birthDateInput = document.getElementById('birthDate');
    if (birthDateInput.value.trim() === "") {
        highlightError(birthDateInput);
        showErrorMessage("birthDate", "Birth date cannot be empty");
        isValid = false;
    } else if (!isValidAge(birthDateInput.value)) {
        highlightError(birthDateInput);
        showErrorMessage("birthDate", "You must be at least 18 years old");
        isValid = false;
    } else {
        removeErrorHighlight(birthDateInput);
        hideErrorMessage("birthDate");
    }

    var descriptionInput = document.getElementById('description');
    if (descriptionInput.value.trim() === "") {
        highlightError(descriptionInput);
        showErrorMessage("description", "Description cannot be empty");
        isValid = false;
    } else {
        removeErrorHighlight(descriptionInput);
        hideErrorMessage("description");
    }

    var expectedSalaryInput = document.getElementById('currentsalary');
    if (expectedSalaryInput.value.trim() === "") {
        highlightError(expectedSalaryInput);
        showErrorMessage("currentsalary", "Expected salary cannot be empty");
        isValid = false;
    } else if (parseInt(expectedSalaryInput.value) < 0) {
        highlightError(expectedSalaryInput);
        showErrorMessage("currentsalary", "Expected salary cannot be negative");
        isValid = false;
    } else {
        removeErrorHighlight(expectedSalaryInput);
        hideErrorMessage("currentsalary");
    }

    return isValid;
}

function highlightError(inputElement) {
    inputElement.classList.add("is-invalid");
}

function removeErrorHighlight(inputElement) {
    inputElement.classList.remove("is-invalid");
}

function showErrorMessage(inputId, message) {
    var errorElement = document.getElementById(inputId + '-error');
    if (!errorElement) {
        var inputElement = document.getElementById(inputId);
        errorElement = document.createElement('div');
        errorElement.id = inputId + '-error';
        errorElement.className = 'invalid-feedback';
        inputElement.parentNode.appendChild(errorElement);
    }
    errorElement.textContent = message;
}

function hideErrorMessage(inputId) {
    var errorElement = document.getElementById(inputId + '-error');
    if (errorElement) {
        errorElement.textContent = '';
    }
}

function isValidAge(dateString) {
    var today = new Date();
    var birthDate = new Date(dateString);
    var age = today.getFullYear() - birthDate.getFullYear();
    var monthDiff = today.getMonth() - birthDate.getMonth();
    if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthDate.getDate())) {
        age--;
    }
    return age >= 18;
}
