function validateUpdateForm() {
    var isValid = true;

    var jobTitle = document.getElementById("job-title-e").value;
    var category = document.getElementById("category-e").value;
    var status = document.getElementById("status-e").value;
    var experience = document.getElementById("experience-e").value;
    var offeredSalary = document.getElementById("offered-salary-e").value;

    if (jobTitle.trim() === "") {
        highlightError("job-title-e");
        showErrorMessage("job-title-e", "Job Title cannot be empty");
        isValid = false;
    } else {
        removeErrorHighlight("job-title-e");
        hideErrorMessage("job-title-e");
    }

    if (offeredSalary.trim() === "" || offeredSalary < 0) {
        highlightError("offered-salary-e");
        if (offeredSalary.trim() === "") {
            showErrorMessage("offered-salary-e", "Offered Salary cannot be empty");
        } else {
            showErrorMessage("offered-salary-e", "Offered Salary cannot be negative");
        }
        isValid = false;
    } else {
        removeErrorHighlight("offered-salary-e");
        hideErrorMessage("offered-salary-e");
    }

    return isValid;
}

function highlightError(fieldId) {
    document.getElementById(fieldId).classList.add('error');
}

function removeErrorHighlight(fieldId) {
    document.getElementById(fieldId).classList.remove('error');
}

function showErrorMessage(fieldId, message) {
    var errorMessageElement = document.createElement('div');
    errorMessageElement.className = 'error-message';
    errorMessageElement.textContent = message;

    var fieldContainer = document.getElementById(fieldId).closest('.quform-element');
    fieldContainer.appendChild(errorMessageElement);
}

function hideErrorMessage(fieldId) {
    var fieldContainer = document.getElementById(fieldId).closest('.quform-element');
    var errorMessageElement = fieldContainer.querySelector('.error-message');
    if (errorMessageElement) {
        errorMessageElement.remove();
    }
}
