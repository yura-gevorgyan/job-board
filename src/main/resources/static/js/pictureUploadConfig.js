function updateFileInput(fileInput, filesArray) {

    const dataTransfer = new DataTransfer();

    filesArray.forEach(file => {
        dataTransfer.items.add(file);
    });

    fileInput.files = dataTransfer.files;
}

function handleFileUpload(event) {
    const fileInput = event.target;
    const files = fileInput.files;
    const gallery = document.getElementById("gallery");

    const uploadedFiles = Array.from(files);


    uploadedFiles.forEach((file, index) => {

        const newPictureContainer = document.createElement("div");
        newPictureContainer.className = "col-md-6 col-lg-4 mt-1-9";

        const newImage = document.createElement("img");
        newImage.className = "border-radius-10 img-hov";
        newImage.style.width = "200px";
        newImage.alt = "...";

        const reader = new FileReader();
        reader.onload = function (e) {
            newImage.src = e.target.result;
        };
        reader.readAsDataURL(file);

        const trashButton = document.createElement("button");
        trashButton.className = "trash-button";
        trashButton.textContent = "🗑️";

        trashButton.onclick = function () {
            uploadedFiles.splice(index, 1);

            newPictureContainer.remove();

            updateFileInput(fileInput, uploadedFiles);
        };

        newPictureContainer.appendChild(newImage);
        newPictureContainer.appendChild(trashButton);

        gallery.appendChild(newPictureContainer);
    });
}

function handleFileUploadForCreate(event) {
    const fileInput = event.target;
    const files = fileInput.files;
    const gallery = document.getElementById("gallery-create");

    const uploadedFiles = Array.from(files);


    uploadedFiles.forEach((file, index) => {

        const newPictureContainer = document.createElement("div");
        newPictureContainer.className = "col-md-6 col-lg-4 mt-1-9";

        const newImage = document.createElement("img");
        newImage.className = "border-radius-10 img-hov";
        newImage.style.width = "200px";
        newImage.alt = "...";

        const reader = new FileReader();
        reader.onload = function (e) {
            newImage.src = e.target.result;
        };
        reader.readAsDataURL(file);

        const trashButton = document.createElement("button");
        trashButton.className = "trash-button";
        trashButton.textContent = "🗑️";

        // Attach the delete function
        trashButton.onclick = function () {
            uploadedFiles.splice(index, 1);

            newPictureContainer.remove();

            updateFileInput(fileInput, uploadedFiles);
        };

        newPictureContainer.appendChild(newImage);
        newPictureContainer.appendChild(trashButton);

        gallery.appendChild(newPictureContainer);
    });
}

const deletedPictures = [];

function deleteImage(buttonElement) {
    const divToRemove = buttonElement.parentElement;
    divToRemove.parentNode.removeChild(divToRemove);

    const pictureName = buttonElement.getAttribute("data-picture-name");

    deletedPictures.push(pictureName);

    const deletedPicturesInput = document.getElementById("deletedPictures");
    deletedPicturesInput.value = deletedPictures.join(",");
}

const form = document.getElementById("yourForm");
form.addEventListener("submit", function (event) {
    const deletedPicturesInput = document.getElementById("deletedPictures");
    deletedPicturesInput.value = deletedPictures.join(",");
});
