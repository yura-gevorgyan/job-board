function validateForm() {
    var newPassword = document.getElementById("password").value;
    var confirmPassword = document.getElementById("confirmPassword").value;

    var isValid = true;

    if (newPassword.trim() === "" || newPassword.length < 8) {
        highlightError("password");
        if (newPassword.trim() === "") {
            showErrorMessage("password", "New Password cannot be empty");
        } else {
            showErrorMessage("password", "New Password must be at least 8 characters long");
        }
        isValid = false;
    } else {
        removeErrorHighlight("password");
        hideErrorMessage("password");
    }

    if (confirmPassword.trim() === "" || confirmPassword !== newPassword) {
        highlightError("confirmPassword");
        if (confirmPassword.trim() === "") {
            showErrorMessage("confirmPassword", "Please confirm your new password");
        } else {
            showErrorMessage("confirmPassword", "Passwords do not match");
        }
        isValid = false;
    } else {
        removeErrorHighlight("confirmPassword");
        hideErrorMessage("confirmPassword");
    }
    return isValid;
}

function highlightError(elementId) {
    document.getElementById(elementId).classList.add("is-invalid");
}

function removeErrorHighlight(elementId) {
    document.getElementById(elementId).classList.remove("is-invalid");
}

function hideErrorMessage(elementId) {
    var errorElement = document.getElementById(elementId + "-error");
    if (errorElement) {
        errorElement.textContent = "";
    }
}

function showErrorMessage(elementId, message) {
    var errorElement = document.getElementById(elementId + "-error");
    if (errorElement) {
        errorElement.textContent = message;
    } else {
        var inputElement = document.getElementById(elementId);
        errorElement = document.createElement("div");
        errorElement.id = elementId + "-error";
        errorElement.className = "invalid-feedback";
        errorElement.textContent = message;
        inputElement.parentNode.appendChild(errorElement);
    }
}
