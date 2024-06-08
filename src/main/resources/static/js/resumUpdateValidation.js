function validateFormUpdate() {

    var errorMessages = document.querySelectorAll(".error-message");
    errorMessages.forEach(function (errorMessage) {
        errorMessage.parentNode.removeChild(errorMessage);
    });

    var allInputs = document.querySelectorAll(".form-control");
    allInputs.forEach(function (input) {
        input.style.borderColor = ""; // Возвращаем стандартный цвет рамки
    });

    var birthDate = document.getElementById("birthDatte");
    var expectedSalary = document.getElementById("expectedSalaryy");
    var profession = document.getElementById("professionn");
    var phoneNumber = document.getElementById("phoneNumber");
    var description = document.getElementById("massage");

    function showError(input, message) {
        input.style.borderColor = "red"; // Устанавливаем цвет рамки поля красным
        var errorPara = document.createElement("p"); // Создаем элемент для вывода сообщения
        errorPara.classList.add("error-message");
        errorPara.style.color = "red";
        errorPara.innerHTML = message;
        input.parentNode.appendChild(errorPara); // Добавляем сообщение об ошибке после поля
    }

    if (!birthDate.value) {
        showError(birthDate, "Please fill in this field.");
    }
    if (!expectedSalary.value) {
        showError(expectedSalary, "Please fill in this field.");
    } else if (expectedSalary.value < 0) {
        showError(expectedSalary, "Expected salary cannot be negative.");
    }
    if (!profession.value) {
        showError(profession, "Please fill in this field.");
    }
    if (!phoneNumber.value) {
        showError(phoneNumber, "Please fill in this field.");
    }
    if (!description.value) {
        showError(description, "Please fill in this field.");
    }

    var currentDate = new Date();
    var selectedDate = new Date(birthDate.value);
    var age = currentDate.getFullYear() - selectedDate.getFullYear();
    var monthDiff = currentDate.getMonth() - selectedDate.getMonth();
    if (monthDiff < 0 || (monthDiff === 0 && currentDate.getDate() < selectedDate.getDate())) {
        age--;
    }

    if (age < 18) {
        showError(birthDate, "You must be at least 18 years old.");
    }
    return !document.querySelectorAll(".error-message").length;
}
