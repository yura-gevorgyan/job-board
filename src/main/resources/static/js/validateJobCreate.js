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

    var jobTitleInput = document.getElementById('job-title');
    if (jobTitleInput.value.trim() === "") {
        highlightError(jobTitleInput);
        showErrorMessage("job-title", "Profession cannot be empty");
        isValid = false;
    } else {
        removeErrorHighlight(jobTitleInput);
        hideErrorMessage("job-title");
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

    var descriptionInput = document.getElementById('job-description');
    if (descriptionInput.value.trim() === "") {
        highlightError(descriptionInput);
        showErrorMessage("job-description", "Description cannot be empty");
        isValid = false;
    } else {
        removeErrorHighlight(descriptionInput);
        hideErrorMessage("job-description");
    }

    var offeredSalaryInput = document.getElementById('offered-salary');
    if (offeredSalaryInput.value.trim() === "") {
        highlightError(offeredSalaryInput);
        showErrorMessage("offered-salary", "Expected salary cannot be empty");
        isValid = false;
    } else if (parseInt(offeredSalaryInput.value) < 0) {
        highlightError(offeredSalaryInput);
        showErrorMessage("offered-salary", "Expected salary cannot be negative");
        isValid = false;
    } else {
        removeErrorHighlight(offeredSalaryInput);
        hideErrorMessage("offered-salary");
    }

    // Проверка категории
    var categorySelect = document.getElementById('category');
    if (categorySelect.value === "") {
        highlightError(categorySelect);
        showErrorMessage("category", "Please select a category");
        isValid = false;
    } else {
        removeErrorHighlight(categorySelect);
        hideErrorMessage("category");
    }

    // Проверка опыта
    var experienceSelect = document.getElementById('experience');
    if (experienceSelect.value === "") {
        highlightError(experienceSelect);
        showErrorMessage("experience", "Please select experience");
        isValid = false;
    } else {
        removeErrorHighlight(experienceSelect);
        hideErrorMessage("experience");
    }

    // Проверка статуса работы
    var jobStatusSelect = document.getElementById('status');
    if (jobStatusSelect.value === "") {
        highlightError(jobStatusSelect);
        showErrorMessage("status", "Please select job status");
        isValid = false;
    } else {
        removeErrorHighlight(jobStatusSelect);
        hideErrorMessage("status");
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
