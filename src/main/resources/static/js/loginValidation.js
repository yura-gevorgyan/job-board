function validateForm() {

    var email = document.getElementById("username").value;
    var password = document.getElementById("password").value;

    var isValid = true;

    if (email.trim() === "" || !isValidEmail(email)) {
        highlightError("username");
        if (email.trim() === "") {
            showErrorMessage("username", "Email cannot be empty");
        } else {
            showErrorMessage("username", "Invalid email format");
        }
        isValid = false;
    } else {
        removeErrorHighlight("username");
        hideErrorMessage("username");
    }

    if (password.trim() === "" || password.length < 8) {
        highlightError("password");
        if (password.trim() === "") {
            showErrorMessage("password", "Password cannot be empty");
        } else {
            showErrorMessage("password", "Password must be at least 8 characters long");
        }
        isValid = false;
    } else {
        removeErrorHighlight("password");
        hideErrorMessage("password");
    }

    return isValid;
}

function highlightError(elementId) {
    document.getElementById(elementId).classList.add("is-invalid");
}

function removeErrorHighlight(elementId) {
    document.getElementById(elementId).classList.remove("is-invalid");
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

function hideErrorMessage(elementId) {
    var errorElement = document.getElementById(elementId + "-error");
    if (errorElement) {
        errorElement.textContent = "";
    }
}

function isValidEmail(email) {
    var emailRegex = /\S+@\S+\.\S+/;
    return emailRegex.test(email);
}
