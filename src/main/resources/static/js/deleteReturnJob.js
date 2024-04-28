let modal = document.getElementById("myModal");
let modalFD = document.getElementById("myModalFD");
let btn = document.getElementsByClassName("getElementByClass");
let delbtn = document.getElementsByClassName("getDeletedElementByClass");
let jobId = null;

Array.from(btn).forEach(function (btn) {
    btn.addEventListener("click", function () {
        modal.style.display = "block";
    });
});

Array.from(delbtn).forEach(function (delbtn) {
    delbtn.addEventListener("click", function () {
        modalFD.style.display = "block";
    });
});

let span = document.getElementsByClassName("close")[0];
span.onclick = function () {
    modal.style.display = "none";
}

let spanFD = document.getElementsByClassName("close-e")[0];
spanFD.onclick = function () {
    modalFD.style.display = "none";
}

window.onclick = function (event) {
    if (event.target === modal) {
        modal.style.display = "none";
    }
    if (event.target === modalFD) {
        modalFD.style.display = "none";
    }
}

document.querySelectorAll('.getElementByClass').forEach(function (element) {
    element.addEventListener("click", saveCurrentUserId);
});

document.querySelectorAll('.getDeletedElementByClass').forEach(function (element) {
    element.addEventListener("click", saveCurrentUserIdFD);
});

function saveCurrentUserId(event) {
    event.preventDefault();

    jobId = event.currentTarget.getAttribute("data-current-job-id");

    if (!jobId) {
        let parent = event.target.closest("[data-current-job-id]");
        if (parent) {
            jobId = parent.getAttribute("data-current-job-id");
        }
    }
}

function saveCurrentUserIdFD(event) {
    event.preventDefault();

    jobId = event.currentTarget.getAttribute("data-current-job-id-fd");

    if (!jobId) {
        let parent = event.target.closest("[data-current-job-id-fd]");
        if (parent) {
            jobId = parent.getAttribute("data-current-job-id-fd");
        }
    }
}

$(document).ready(function () {

    $("#deleteBtnFD").on("click", function () {

        $.ajax({
            url: "/jobs/return/" + jobId,
            type: "POST",
            success: function (response) {

                console.log("Job returned successfully:", response);

                let jobRow = $(`a[data-current-job-id-fd='${jobId}']`).closest("tr");

                jobRow.find("td:nth-child(4)").html('<span class="text-success font-weight-600">Active</span>');

                jobRow.find("ul.action-list").hide();

                modalFD.style.display = "none";
            },
            error: function (xhr, status, error) {

                console.error("Error deleting job:", error);

            }
        });
    });
});

$(document).ready(function () {

    $("#deleteBtn").on("click", function () {

        $.ajax({
            url: "/jobs/delete/" + jobId,
            type: "POST",
            success: function (response) {

                console.log("Job deleted successfully:", response);

                let jobRow = $(`a[data-current-job-id='${jobId}']`).closest("tr");

                // Change the 'Status' column to 'Deleted'
                jobRow.find("td:nth-child(4)").html('<span class="text-danger font-weight-600">Deleted</span>');

                // Hide the 'Action' column for the deleted job
                jobRow.find("ul.action-list").hide();

                modal.style.display = "none";
            },
            error: function (xhr, status, error) {

                console.error("Error deleting job:", error);

            }
        });
    });
});